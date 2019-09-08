package com.passport.service.worker;

import com.common.util.StringUtils;
import com.common.util.model.YesOrNoEnum;
import com.passport.domain.MailInfo;
import com.passport.domain.module.MailInfoStatusEnum;
import com.passport.service.MailInfoService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by shilun on 2016/8/15.
 */
@Service
public class MailWork {
    private static Logger logger = LoggerFactory.getLogger(MailWork.class);
    @Resource
    private MailInfoService mailInfoService;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    volatile AtomicBoolean runing = new AtomicBoolean(false);

    public void execute() {
        if (!runing.getAndSet(true)) {
            MailInfo query = new MailInfo();
            query.setStatus(MailInfoStatusEnum.Wait.getValue());
            query.setDelStatus(YesOrNoEnum.NO.getValue());
            query.setExecCount(0);
            Page<MailInfo> list = mailInfoService.queryByPage(query, new PageRequest(0, 20));
            for (MailInfo mail : list) {
                doSendMail(mail);
            }
            runing.set(false);
        }
    }

    public void doSendMail(MailInfo mail) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                ByteArrayInputStream stream = null;
                try {
                    MimeMessage msg = mailSender.createMimeMessage();
                    MimeMessageHelper msgHelper = new MimeMessageHelper(msg);
                    msgHelper.setFrom("test@nidone.com.cn");
                    msgHelper.setTo(mail.getRecipient());
                    msgHelper.setSubject(mail.getTitle());
                    String fileName = (mail.getFileName());
                    Multipart multipart = new MimeMultipart();
                    BodyPart contentPart = new MimeBodyPart();
                    contentPart.setContent(mail.getContent(), "text/html;charset=UTF-8");
                    multipart.addBodyPart(contentPart);
                    if (StringUtils.isNotBlank(fileName)) {
                        stream = new ByteArrayInputStream(mail.getAttchMentFile());
                        DataSource dataSource = new ByteDataSource(fileName, stream);
                        BodyPart attachmentBodyPart = new MimeBodyPart();
                        attachmentBodyPart.setDataHandler(new DataHandler(dataSource));
                        attachmentBodyPart.setFileName(MimeUtility.encodeWord(fileName));
                        multipart.addBodyPart(attachmentBodyPart);
                    }
                    msg.setContent(multipart);
                    mailSender.send(msg);
                    mailInfoService.sendSuccess(mail.getId());
                } catch (Exception e) {
                    logger.error("send mail error", e);
                    mailInfoService.doUpExecCount(mail.getId(), mail.getExecCount() + 1);
                } finally {
                    IOUtils.closeQuietly(stream);
                }
            }
        };
        thread.start();
    }

    class ByteDataSource implements DataSource {
        private String fileName;
        private InputStream stream;

        public ByteDataSource(String fileName, InputStream stream) {
            this.fileName = fileName;
            this.stream = stream;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return stream;
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            return null;
        }

        @Override
        public String getContentType() {
            return "application/octet-stream";
        }

        @Override
        public String getName() {
            return fileName;
        }
    }

}

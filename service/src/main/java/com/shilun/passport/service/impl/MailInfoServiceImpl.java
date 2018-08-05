package com.shilun.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.common.util.RPCResult;
import com.common.util.StringUtils;
import com.shilun.passport.domain.MailInfo;
import com.shilun.passport.domain.module.MailInfoStatusEnum;
import com.shilun.passport.service.MailInfoService;
import com.shilun.passport.service.dto.MailInfoDto;
import com.shilun.passport.service.worker.MailWork;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Date;

/**
 * Created by shilun on 2016/8/15.
 */
@Service
public class MailInfoServiceImpl extends AbstractMongoService<MailInfo> implements MailInfoService {
    @Resource
    private MailWork mailWork;

    @Override
    protected Class getEntityClass() {
        return MailInfo.class;
    }

    @Override
    public Long insert(MailInfo entity) {
        entity.setExecCount(0);
        entity.setStatus(MailInfoStatusEnum.Wait.getValue());
        return super.insert(entity);
    }

    public RPCResult<Boolean> sendMail(String content, String source) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            byte[] bytes = Base64.decodeBase64(content);
            ByteArrayInputStream bIn = new ByteArrayInputStream(bytes);
            ObjectInputStream objIn = new ObjectInputStream(bIn);
            MailInfoDto dto = (MailInfoDto) objIn.readObject();
            MailInfo entity = new MailInfo();
            entity.setContent(dto.getContent());
            entity.setRecipient(dto.getRecipient());
            entity.setSender(source);
            entity.setTitle(dto.getTitle());
            if (dto.getAttchMentFile() != null) {
                entity.setAttchMentFile(dto.getAttchMentFile());
            }
            if (StringUtils.isNotBlank(dto.getFileName())) {
                entity.setFileName(dto.getFileName());
            }
            insert(entity);
            mailWork.doSendMail(entity);
            result.setData(true);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setData(false);
            result.setSuccess(false);
            result.setCode(source + "sendmail.error");
            result.setMessage("发送邮件成功");
        }
        return result;
    }


    @Override
    public void sendSuccess(Long mailId) {
        MailInfo mail = new MailInfo();
        mail.setId(mailId);
        mail.setStatus(MailInfoStatusEnum.SendSuccess.getValue());
        mail.setSendTime(new Date());
        up(mail);
    }

    @Override
    public void doUpExecCount(Long mailId, Integer execuCount) {
        MailInfo mail = new MailInfo();
        mail.setId(mailId);
        mail.setExecCount(execuCount);
        up(mail);
    }

}

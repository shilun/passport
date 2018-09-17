package com.passport.service.worker;

import com.common.util.model.YesOrNoEnum;
import com.passport.domain.SMSInfo;
import com.passport.service.SMSInfoService;
import com.passport.service.util.AliyunMnsUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 短信发送worker
 * Created by shilun on 10/26/16.
 */
@Service
public class SMSWorker {

    private static final Logger logger = Logger.getLogger(SMSWorker.class);

    @Autowired(required =false)
    private AliyunMnsUtil aliyunMnsUtil;

    @Resource
    private SMSInfoService smsInfoService;

    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

    public void execute() {
        Pageable page = new PageRequest(0, 100);
        SMSInfo entity = new SMSInfo();
        entity.setStatus(YesOrNoEnum.NO.getValue());
        entity.setMinExecuteCount(3);
        entity.setStatus(YesOrNoEnum.NO.getValue());
        Page<SMSInfo> smsInfos = smsInfoService.queryByPage(entity, page);
        for (SMSInfo item : smsInfos.getContent()) {
            execute(item);
        }
    }



    public void execute(final SMSInfo item) {
        Runnable run = () -> {
            SMSInfo data = item;
            try {
                aliyunMnsUtil.sendSms(
                        item.getId(),
                        data.getContent(),
                        data.getMobile());
                sendSuccess(data.getId());
            } catch (Exception e) {
                logger.error("sendSMS error:content =>", e);
                data.setExecuteCount(data.getExecuteCount() + 1);
            }
        };

        fixedThreadPool.execute(run);
    }


    //短信发送成功
    protected void sendSuccess(Long id) {
        SMSInfo entity = new SMSInfo();
        entity.setId(id);
        entity.setStatus(YesOrNoEnum.YES.getValue());
        smsInfoService.save(entity);
    }
}

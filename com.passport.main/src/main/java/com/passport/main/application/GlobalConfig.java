package com.passport.main.application;

import com.common.upload.UploadUtil;
import com.passport.service.util.AliyunMnsUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class GlobalConfig {

    @Value("${app.sms.aliyun.accessId}")
    private String smsAliyunAccessId;
    @Value("${app.sms.aliyun.accessKey}")
    private String smsAliyunAccessKey;
    @Value("${app.sms.aliyun.endpoint}")
    private String smsAliyunEndpoint;
    @Value("${app.sms.aliyun.smsTopic}")
    private String smsAliyunSmsTopic;
    @Value("${app.software.upload.scode}")
    private String softUploadScode;
    @Value("${app.software.upload.code}")
    private String softUploadCode;
    @Value("${app.software.upload.domain}")
    private String softUploadDomain;

    @Bean("aliyunMnsUtil")
    public AliyunMnsUtil AliyunMnsUtil() {
        AliyunMnsUtil aliyunMnsUtil = new AliyunMnsUtil();
        aliyunMnsUtil.setAccessId(smsAliyunAccessId);
        aliyunMnsUtil.setAccessKey(smsAliyunAccessKey);
        aliyunMnsUtil.setEndpoint(smsAliyunEndpoint);
        aliyunMnsUtil.setSmsTopic(smsAliyunSmsTopic);
        ClassPathResource resource = new ClassPathResource("sms-template.xml");
        aliyunMnsUtil.setTemplateXml(resource);
        return aliyunMnsUtil;
    }
    @Bean("softUploadUtil")
   public UploadUtil uploadUtil(){
        UploadUtil uploadUtil=new UploadUtil();
        uploadUtil.setScode(softUploadScode);
        uploadUtil.setDomainName(softUploadDomain);
        uploadUtil.setCode(softUploadCode);
        return uploadUtil;
    }
}

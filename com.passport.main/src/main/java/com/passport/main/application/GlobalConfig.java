package com.passport.main.application;

import com.common.upload.UploadUtil;
import com.passport.service.util.AliyunMnsUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class  GlobalConfig {

    @Value("${app.software.upload.scode}")
    private String softUploadScode;
    @Value("${app.software.upload.code}")
    private String softUploadCode;
    @Value("${app.software.upload.domain}")
    private String softUploadDomain;
    @Bean("softUploadUtil")
   public UploadUtil uploadUtil(){
        UploadUtil uploadUtil=new UploadUtil();
        uploadUtil.setScode(softUploadScode);
        uploadUtil.setDomainName(softUploadDomain);
        uploadUtil.setCode(softUploadCode);
        return uploadUtil;
    }
}

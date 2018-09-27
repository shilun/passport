package com.passport.web.application;

import com.common.upload.UploadUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: CSL
 * @Date: 2018/9/6 10:39
 */

@Configuration
public class UploadCfg {
    @Value("${app.upload.code}")
    private String code;
    @Value("${app.upload.scode}")
    private String scode;
    @Value("${app.upload.domain}")
    private String domain;
    @Bean
    public UploadUtil uploadUtil(){
        UploadUtil uploadUtil=new UploadUtil();
        uploadUtil.setCode(code);
        uploadUtil.setDomainName(domain);
        uploadUtil.setScode(scode);
        return uploadUtil;
    }
}

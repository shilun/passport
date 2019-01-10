package com.passport.web.application;

import com.passport.service.util.AliyunMnsUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Properties;

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


    public static void main(String[] args) throws Exception {
        Properties prop = new Properties();
        BufferedInputStream in = new BufferedInputStream(new FileInputStream("D:\\workspace\\passport\\com.passport.web\\src\\main\\resources\\application.properties"));
        //prop.load(in);//直接这么写，如果properties文件中有汉子，则汉字会乱码。因为未设置编码格式。
        prop.load(new InputStreamReader(in, "gbk"));
        System.out.println(1);
    }


}

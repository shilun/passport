package com.passport.web.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

@SpringBootConfiguration
public class ConfigurerAdapter extends WebMvcConfigurerAdapter {
    @Resource
    private UserSessionHandlerInterceptor resourceHandlerInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(3600)
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(resourceHandlerInterceptor).addPathPatterns("/**").excludePathPatterns(new String[]{"error",
                "/login/*",
                "/qrCode/*",
                "/AppDownload/*",
                "/appinterface/*"});
    }
}

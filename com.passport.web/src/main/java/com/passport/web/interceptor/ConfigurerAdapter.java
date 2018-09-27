package com.passport.web.interceptor;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

@SpringBootConfiguration
public class ConfigurerAdapter extends WebMvcConfigurerAdapter {
    @Resource
    private UserSessionHandlerInterceptor resourceHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(resourceHandlerInterceptor).addPathPatterns("/**").excludePathPatterns(new String[]{"/error",
               "/login/*",
               "/appinterface/user-login",
               "/appinterface/user-reg",
               "/appinterface/forgetPassBuildCode",
               "/appinterface/regBuildCode"});
    }
}

package com.passport.main.interceptor;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

@SpringBootConfiguration
public class ConfigurerAdapter extends WebMvcConfigurerAdapter {
    @Resource
    private ResourceHandlerInterceptor resourceHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(resourceHandlerInterceptor).addPathPatterns("/**").excludePathPatterns(new String[]{
               "/login/*"});
    }
}

package com.passport.web.controller;

import com.passport.domain.AppConfig;
import com.passport.service.AppConfigService;
import com.passport.web.AbstractClientController;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@RestController
public class ConfigController extends AbstractClientController {

    @Resource
    private AppConfigService appConfigService;


    @RequestMapping(value = "appConfig", method = {RequestMethod.GET})
    @ResponseBody
    public String AppConfig() {
        Long id = getDomain().getId();
        return appConfigService.findByProxyId(id);
    }
}

package com.passport.web.controller;

import com.passport.domain.AppConfig;
import com.passport.service.AppConfigService;
import com.passport.web.AbstractClientController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ConfigController extends AbstractClientController {

    @Resource
    private AppConfigService appConfigService;

    @RequestMapping(value = "AppConfig", method = {RequestMethod.GET})
    public String AppConfig() {
        Long id = getDomain().getId();
        AppConfig config = new AppConfig();
        config.setProxyId(id);
        return appConfigService.findByOne(config).getContent();
    }
}

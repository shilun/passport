package com.passport.web.controller;

import com.passport.service.AppConfigService;
import com.passport.web.AbstractClientController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ConfigController extends AbstractClientController {

    @Resource
    private AppConfigService appConfigService;


    @RequestMapping(value = "/appConfig")
    @ResponseBody
    public String AppConfig(HttpServletResponse rsp) {
        Long id = getDomain().getId();
        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
        return appConfigService.findByProxyId(id);
    }
}

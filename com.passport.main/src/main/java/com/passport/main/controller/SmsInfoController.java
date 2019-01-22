package com.passport.main.controller;

import com.common.annotation.RoleResource;
import com.common.util.BeanCoper;
import com.passport.domain.MailInfo;
import com.passport.domain.SMSInfo;
import com.passport.main.AbstractClientController;
import com.passport.main.controller.dto.SmsInfoDto;
import com.passport.service.MailInfoService;
import com.passport.service.SMSInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@Api(description = "短信管理")
@RestController
@RequestMapping(name = "/", method = {RequestMethod.POST})
public class SmsInfoController extends AbstractClientController {
    @Resource
    private SMSInfoService smsInfoService;

    /**
     * 查询
     *
     * @param dto
     * @return
     */
    @RoleResource(resource = "passport")
    @ApiOperation(value = "查询")
    @RequestMapping("/sms/list")
    public Map<String, Object> list(@RequestBody SmsInfoDto dto) {
        return buildMessage(() -> {
            SMSInfo t = BeanCoper.copyProperties(SMSInfo.class, dto);
            return smsInfoService.queryByPage(t, dto.getPageinfo().getPage());
        });
    }


}

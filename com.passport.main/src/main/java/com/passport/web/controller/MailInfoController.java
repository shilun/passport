package com.passport.web.controller;

import com.common.annotation.RoleResource;
import com.passport.domain.MailInfo;
import com.passport.service.MailInfoService;
import com.passport.web.AbstractClientController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@Api(description = "邮件管理")
@RestController
@RequestMapping(name = "/", method = {RequestMethod.POST})
public class MailInfoController extends AbstractClientController {
    @Resource
    private MailInfoService mailInfoService;

    /**
     * 查询
     *
     * @param info
     * @return
     */
    @RoleResource(resource = "admin")
    @ApiOperation(value = "查询")
    @RequestMapping("/mail/list")
    public Map<String, Object> list(@RequestBody MailInfo info) {
        return buildMessage(() -> mailInfoService.query(info));
    }

    /**
     * 查询
     *
     * @param content
     * @return
     */
    @RoleResource(resource = "admin")
    @ApiOperation(value = "保存")
    @RequestMapping("/mail/view")
    public Map<String, Object> view(@RequestBody String content) {
        return buildMessage(() -> mailInfoService.findById(getIdByJson(content)));
    }

    /**
     * 保存
     *
     * @param info
     * @return
     */
    @RoleResource(resource = "admin")
    @ApiOperation(value = "保存")
    @RequestMapping("/mail/save")
    public Map<String, Object> save(@RequestBody MailInfo info) {
        return buildMessage(() -> mailInfoService.save(info));
    }
}

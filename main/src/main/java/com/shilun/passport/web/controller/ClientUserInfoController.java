package com.shilun.passport.web.controller;

import com.common.web.IExecute;
import com.shilun.passport.domain.ClientUserInfo;
import com.shilun.passport.service.ClientUserInfoService;
import com.shilun.passport.web.AbstractClientController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Api(description = "用户管理")
@RestController
@RequestMapping(method = {RequestMethod.POST})
public class ClientUserInfoController extends AbstractClientController {
    @Resource
    private ClientUserInfoService clientUserInfoService;

    /**
     * 查询
     *
     * @param info
     * @return
     */
    @ApiOperation(value = "查询")
    @RequestMapping("/user/query")
    @ResponseBody
    public Map<String, Object> query(@RequestBody ClientUserInfo info) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                return clientUserInfoService.query(info);
            }
        });
    }
    /**
     * 查询
     *
     * @param content
     * @return
     */
    @ApiOperation(value = "保存")
    @RequestMapping("/user/find")
    @ResponseBody
    public Map<String, Object> find(@RequestBody String content) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                return clientUserInfoService.findById(getIdByJson(content));
            }
        });
    }
    /**
     * 保存
     *
     * @param info
     * @return
     */
    @ApiOperation(value = "保存")
    @RequestMapping("/user/save")
    @ResponseBody
    public Map<String, Object> save(@RequestBody ClientUserInfo info) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                return clientUserInfoService.save(info);
            }
        });
    }
}

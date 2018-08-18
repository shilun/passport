package com.shilun.passport.web.controller;

import com.common.web.IExecute;
import com.shilun.passport.domain.RoleInfo;
import com.shilun.passport.service.RoleInfoService;
import com.shilun.passport.web.AbstractClientController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Api(description = "角色管理")
@RestController
@RequestMapping(method = {RequestMethod.POST})
public class RoleInfoController extends AbstractClientController {
    @Resource
    private RoleInfoService roleInfoService;

    /**
     * 查询
     *
     * @param info
     * @return
     */
    @ApiOperation(value = "查询")
    @RequestMapping("/role/query")
    @ResponseBody
    public Map<String, Object> query(@RequestBody RoleInfo info) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                return roleInfoService.query(info);
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
    @RequestMapping("/role/find")
    @ResponseBody
    public Map<String, Object> find(@RequestBody String content) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                return roleInfoService.findById(getIdByJson(content));
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
    @RequestMapping("/role/save")
    @ResponseBody
    public Map<String, Object> save(@RequestBody RoleInfo info) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                return roleInfoService.save(info);
            }
        });
    }
}

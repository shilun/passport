package com.shilun.passport.web.controller;

import com.common.util.BeanCoper;
import com.common.web.IExecute;
import com.shilun.passport.domain.AdminUserInfo;
import com.shilun.passport.domain.RoleInfo;
import com.shilun.passport.service.RoleInfoService;
import com.shilun.passport.web.AbstractClientController;
import com.shilun.passport.web.controller.dto.RoleDto;
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
    @RequestMapping("/role/list")
    @ResponseBody
    public Map<String, Object> list(@RequestBody RoleDto info) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                RoleInfo entity=new RoleInfo();
                BeanCoper.copyProperties(entity,info);
                return roleInfoService.queryByPage(entity,info.getPageinfo().getPage());
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
    @RequestMapping("/role/view")
    @ResponseBody
    public Map<String, Object> view(@RequestBody String content) {
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
    public Map<String, Object> save(@RequestBody RoleDto info) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                RoleInfo entity=new RoleInfo();
                BeanCoper.copyProperties(entity,info);
                return roleInfoService.save(entity);
            }
        });
    }
}

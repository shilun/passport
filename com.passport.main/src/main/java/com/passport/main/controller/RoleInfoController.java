package com.passport.main.controller;

import com.common.annotation.RoleResource;
import com.common.util.BeanCoper;
import com.passport.domain.RoleInfo;
import com.passport.service.RoleInfoService;
import com.passport.main.AbstractClientController;
import com.passport.main.controller.dto.RoleDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @RoleResource(resource = "passport")
    @ApiOperation(value = "查询")
    @RequestMapping("/role/list")
    public Map<String, Object> list(@RequestBody RoleDto info) {
        return buildMessage(() -> {
            RoleInfo entity = new RoleInfo();
            BeanCoper.copyProperties(entity, info);
            return roleInfoService.queryByPage(entity, info.getPageinfo().getPage());
        });
    }

    /**
     * 查询
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "保存")
    @RequestMapping("/role/view")
    @RoleResource(resource = "passport")
    public Map<String, Object> view(@RequestBody Map<String, String> params) {
        return buildMessage(() -> roleInfoService.findById(params.get("id")));
    }

    /**
     * 保存
     *
     * @param info
     * @return
     */
    @ApiOperation(value = "保存")
    @RequestMapping("/role/save")
    @RoleResource(resource = "passport")
    public Map<String, Object> save(@RequestBody RoleDto info) {
        return buildMessage(() -> {
            RoleInfo entity = new RoleInfo();
            BeanCoper.copyProperties(entity, info);
            roleInfoService.save(entity);
            return null;
        });
    }
}

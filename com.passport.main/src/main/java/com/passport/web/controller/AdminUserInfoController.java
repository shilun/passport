package com.passport.web.controller;

import com.common.annotation.RoleResource;
import com.common.util.BeanCoper;
import com.common.util.model.SexEnum;
import com.passport.domain.AdminUserInfo;
import com.passport.web.controller.dto.AdminDto;
import com.passport.service.AdminUserInfoService;
import com.passport.web.AbstractClientController;
import com.passport.web.controller.dto.IdChangePassDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Api(description = "管理员管理")
@RestController
@RequestMapping(method = {RequestMethod.POST})
public class AdminUserInfoController extends AbstractClientController {

    @Resource
    private AdminUserInfoService adminUserInfoService;

    /**
     * 查询
     *
     * @param info
     * @return
     */
    @RoleResource(resource = "admin")
    @ApiOperation(value = "查询")
    @RequestMapping("/admin/list")
    @ResponseBody
    public Map<String, Object> list(@RequestBody AdminDto info) {
        return buildMessage(() -> {
            AdminUserInfo query = new AdminUserInfo();
            BeanCoper.copyProperties(query, info);
            return adminUserInfoService.queryByPage(query, info.getPageinfo().getPage());
        });
    }

    /**
     * 查询
     *
     * @param content
     * @return
     */
    @RoleResource(resource = "admin")
    @ApiOperation(value = "保存")
    @RequestMapping("/admin/view")
    public Map<String, Object> view(@RequestBody String content) {
        return buildMessage(() ->
                adminUserInfoService.findById(getIdByJson(content)));
    }

    @RoleResource(resource = "admin")
    @RequestMapping("/admin/changePass")
    public Map<String, Object> changePass(@RequestBody IdChangePassDto dto) {
        return buildMessage(() -> {
            adminUserInfoService.changePass(dto.getId(), dto.getPassword());
            return null;
        });
    }

    /**
     * 保存
     *
     * @param info
     * @return
     */
    @RoleResource(resource = "admin")
    @ApiOperation(value = "保存")
    @RequestMapping("/admin/save")
    @ResponseBody
    public Map<String, Object> save(@RequestBody AdminDto info) {
        return buildMessage(() -> {
            AdminUserInfo entity = new AdminUserInfo();
            BeanCoper.copyProperties(entity, info);
            adminUserInfoService.save(entity);
            return null;
        });
    }

}

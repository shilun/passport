package com.passport.main.controller;

import com.common.annotation.RoleResource;
import com.common.util.BeanCoper;
import com.passport.domain.AdminUserInfo;
import com.passport.main.AbstractClientController;
import com.passport.main.controller.dto.AdminDto;
import com.passport.main.controller.dto.IdChangePassDto;
import com.passport.service.AdminUserInfoService;
import com.passport.service.OperatorLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Api(description = "管理员管理")
@RestController
@RequestMapping(method = {RequestMethod.POST})
public class AdminUserInfoController extends AbstractClientController {

    @Resource
    private AdminUserInfoService adminUserInfoService;

    @Resource
    private OperatorLogService operatorLogService;
    /**
     * 查询
     *
     * @param info
     * @return
     */
    @RoleResource(resource = "passport")
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
     * @return
     */
    @RoleResource(resource = "passport")
    @ApiOperation(value = "保存")
    @RequestMapping("/admin/view")
    public Map<String, Object> view(@RequestBody Map<String, String> params) {
        return buildMessage(() ->
                adminUserInfoService.findById(params.get("id")));
    }

    @RoleResource(resource = "passport")
    @RequestMapping("/admin/changePass")
    public Map<String, Object> changePass(@RequestBody IdChangePassDto dto) {
        return buildMessage(() -> {
            operatorLogService.logInfo("passport",getPin(),"/admin/changePass", null);
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
    @RoleResource(resource = "passport")
    @ApiOperation(value = "保存")
    @RequestMapping("/admin/save")
    @ResponseBody
    public Map<String, Object> save(@RequestBody AdminDto info) {
        return buildMessage(() -> {
            operatorLogService.logInfo("passport",getPin(),"/admin/save", JSONObject.fromObject(info).toString());
            AdminUserInfo entity = new AdminUserInfo();
            BeanCoper.copyProperties(entity, info);
            adminUserInfoService.save(entity);
            return null;
        });
    }

}

package com.shilun.passport.web.controller;

import com.common.util.BeanCoper;
import com.common.web.IExecute;
import com.shilun.passport.domain.AdminUserInfo;
import com.shilun.passport.service.AdminUserInfoService;
import com.shilun.passport.web.AbstractClientController;
import com.shilun.passport.web.controller.dto.AdminDto;
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
    @ApiOperation(value = "查询")
    @RequestMapping("/admin/list")
    @ResponseBody
    public Map<String, Object> list(@RequestBody AdminDto info) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                AdminUserInfo query=new AdminUserInfo();
                BeanCoper.copyProperties(query,info);
                return adminUserInfoService.queryByPage(query,info.getPageinfo().getPage());
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
    @RequestMapping("/admin/view")
    @ResponseBody
    public Map<String, Object> view(@RequestBody String content) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                return adminUserInfoService.findById(getIdByJson(content));
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
    @RequestMapping("/admin/save")
    @ResponseBody
    public Map<String, Object> save(@RequestBody AdminDto info) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                AdminUserInfo entity=new AdminUserInfo();
                BeanCoper.copyProperties(entity,info);
                adminUserInfoService.save(entity);
                return null;
            }
        });
    }

}

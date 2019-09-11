package com.passport.main.controller;

import com.common.annotation.RoleResource;
import com.common.util.BeanCoper;
import com.passport.domain.ClientUserInfo;
import com.passport.main.AbstractClientController;
import com.passport.main.controller.dto.ClientUserDto;
import com.passport.service.ClientUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @RoleResource(resource = "passport")
    @ApiOperation(value = "查询")
    @RequestMapping("/user/list")
    @ResponseBody
    public Map<String, Object> list(@RequestBody ClientUserDto info) {
        return buildMessage(() -> {
            ClientUserInfo entity = new ClientUserInfo();
            BeanCoper.copyProperties(entity, info);
            return clientUserInfoService.queryByPage(entity, info.getPageinfo().getPage());
        });
    }
}

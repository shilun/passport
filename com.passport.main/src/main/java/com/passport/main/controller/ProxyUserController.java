package com.passport.main.controller;

import com.common.annotation.RoleResource;
import com.common.util.BeanCoper;
import com.passport.domain.ProxyUserInfo;
import com.passport.main.AbstractClientController;
import com.passport.main.controller.dto.ProxyUserDto;
import com.passport.main.controller.dto.ProxyUserPasswordChangeDto;
import com.passport.service.ProxyUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @date 2018/10/8 10:07
 */
@Api(description = "代理商用户管理")
@RestController
@RequestMapping(method = {RequestMethod.POST})
public class ProxyUserController extends AbstractClientController {
    @Resource
    private ProxyUserInfoService proxyUserInfoService;


    /**
     * 获取配置
     *
     * @param dto
     * @return
     */
    @RoleResource(resource = "passport")
    @RequestMapping("/proxyuser/list")
    public Map<String, Object> queryProxyServers(@RequestBody ProxyUserDto dto) {
        return buildMessage(() -> {
            ProxyUserInfo info = new ProxyUserInfo();
            BeanCoper.copyProperties(info, dto);
            Page<ProxyUserInfo> proxyUserInfos = proxyUserInfoService.queryByPage(info, dto.getPageinfo().getPage());
            return proxyUserInfos;
        });
    }


    /**
     * 查询
     *
     * @param content
     * @return
     */
    @ApiOperation(value = "保存")
    @RequestMapping("/proxyuser/view")
    @RoleResource(resource = "passport")
    public Map<String, Object> view(@RequestBody String content) {
        return buildMessage(() ->
                proxyUserInfoService.findById(getIdByJson(content))
        );
    }


    /**
     * 保存
     *
     * @param dto
     * @return
     */
    @ApiOperation(value = "保存")
    @RequestMapping("/proxyuser/save")
    @RoleResource(resource = "passport")
    public Map<String, Object> save(@RequestBody ProxyUserDto dto) {
        return buildMessage(() -> {
            ProxyUserInfo entity = new ProxyUserInfo();
            entity.setProxyId(getUser().getProxyId());
            BeanCoper.copyProperties(entity, dto);
            proxyUserInfoService.save(entity);
            return null;
        });
    }

    @ApiOperation(value = "修改密码")
    @RequestMapping("/proxyuser/changePass")
    @RoleResource(resource = "passport")
    public Map<String, Object> changePass(@RequestBody ProxyUserPasswordChangeDto dto) {
        return buildMessage(() -> {
            proxyUserInfoService.changePass(dto.getProxyId(), dto.getUserId(), dto.getOldPassword(), dto.getNewPassword());
            return null;
        });
    }


}

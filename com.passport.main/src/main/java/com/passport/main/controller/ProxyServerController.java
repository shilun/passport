package com.passport.main.controller;

import com.common.annotation.RoleResource;
import com.common.util.BeanCoper;
import com.passport.domain.ProxyServerInfo;
import com.passport.main.AbstractClientController;
import com.passport.main.controller.dto.ProxyServerDto;
import com.passport.service.ProxyServerInfoService;
import com.passport.service.util.Tool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Luo
 * @date 2018/10/8 10:07
 */
@Api(description = "服务器配置管理")
@RestController
@RequestMapping(method = {RequestMethod.POST})
public class ProxyServerController extends AbstractClientController {
    @Resource
    private ProxyServerInfoService proxyServerInfoService;
    @Resource
    private Tool tool;


    /**
     * 获取配置
     *
     * @param dto
     * @return
     */
    @RoleResource(resource = "passport")
    @RequestMapping("/server/list")
    public Map<String, Object> queryProxyServers(@RequestBody ProxyServerDto dto) {
        return buildMessage(() -> {
            ProxyServerInfo info = new ProxyServerInfo();
            BeanCoper.copyProperties(info, dto);
            return proxyServerInfoService.queryByPage(info, dto.getPageinfo().getPage());
        });
    }


    /**
     * 查询
     *
     * @param content
     * @return
     */
    @ApiOperation(value = "保存")
    @RequestMapping("/server/view")
    @RoleResource(resource = "passport")
    public Map<String, Object> view(@RequestBody String content) {
        return buildMessage(() ->
                proxyServerInfoService.findById(getIdByJson(content))
        );
    }


    /**
     * 保存
     *
     * @param dto
     * @return
     */
    @ApiOperation(value = "保存")
    @RequestMapping("/server/save")
    @RoleResource(resource = "passport")
    public Map<String, Object> save(@RequestBody ProxyServerDto dto) {
        return buildMessage(() -> {
            ProxyServerInfo entity = new ProxyServerInfo();
            entity.setProxyId(getUser().getProxyId());
            BeanCoper.copyProperties(entity, dto);
            return proxyServerInfoService.save(entity);
        });
    }


}

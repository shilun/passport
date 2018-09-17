package com.passport.web.controller;

import com.common.annotation.RoleResource;
import com.common.util.BeanCoper;
import com.passport.domain.ProxyInfo;
import com.passport.service.ClientUserInfoService;
import com.passport.service.ProxyInfoService;
import com.passport.web.AbstractClientController;
import com.passport.web.controller.dto.ProxyDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping(method = {RequestMethod.POST})
public class ProxyInfoController extends AbstractClientController {
    @Resource
    private ProxyInfoService proxyInfoService;
    @Resource
    private ClientUserInfoService userInfoService;

    /**
     * 查询
     *
     * @param info
     * @return
     */
    @RoleResource(resource = "passport")
    @RequestMapping("/proxy/list")
    public Map<String, Object> list(@RequestBody ProxyDto info) {
        return buildMessage(() -> {
            ProxyInfo entity = new ProxyInfo();
            BeanCoper.copyProperties(entity, info);
            return proxyInfoService.queryByPage(entity, info.getPageinfo().getPage());
        });
    }

    /**
     * 查询
     *
     * @param content
     * @return
     */
    @RoleResource(resource = "passport")
    @RequestMapping("/proxy/view")
    public Map<String, Object> view(@RequestBody String content) {
        return buildMessage(() -> proxyInfoService.findById(getIdByJson(content)));
    }

    /**
     * 保存
     *
     * @param info
     * @return
     */
    @RequestMapping("/proxy/save")
    @RoleResource(resource = "passport")
    public Map<String, Object> save(@RequestBody ProxyDto info) {
        return buildMessage(() -> {
            ProxyInfo entity = new ProxyInfo();
            BeanCoper.copyProperties(entity, info);
            return proxyInfoService.save(entity);
    });
}
}

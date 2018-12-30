package com.passport.main.controller;

import com.common.annotation.RoleResource;
import com.common.util.BeanCoper;
import com.common.util.DateUtil;
import com.passport.domain.AppConfig;
import com.passport.domain.ProxyInfo;
import com.passport.main.AbstractClientController;
import com.passport.main.controller.dto.AppConfigDto;
import com.passport.main.controller.dto.ProxyDto;
import com.passport.service.AppConfigService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping(method = {RequestMethod.POST})
public class ProxyConfigController extends AbstractClientController {
    @Resource
    private AppConfigService appConfigService;

    /**
     * 查询
     * @param info
     * @return
     */
    @RoleResource(resource = "passport")
    @RequestMapping("/proxyconfig/list")
    public Map<String, Object> list(@RequestBody AppConfigDto info) {
        return buildMessage(() -> {
            AppConfig entity = new AppConfig();
            BeanCoper.copyProperties(entity, info);
            return appConfigService.queryByPage(entity, info.getPageinfo().getPage());
        });
    }

    /**
     * 查询
     *
     * @param content
     * @return
     */
    @RoleResource(resource = "passport")
    @RequestMapping("/proxyconfig/view")
    public Map<String, Object> view(@RequestBody String content) {
        return buildMessage(() ->{
            AppConfig byId = appConfigService.findById(getIdByJson(content));
            return byId;
        } );
    }

    /**
     * 保存
     *
     * @param info
     * @return
     */
    @RequestMapping("/proxyconfig/save")
    @RoleResource(resource = "passport")
    public Map<String, Object> save(@RequestBody AppConfigDto info) {
        return buildMessage(() -> {
            AppConfig entity = new AppConfig();
            entity.setProxyId(info.getProxyId());
            entity.setContent(info.getContent());
            entity.setId(info.getId());
            return appConfigService.save(entity);
        });
    }

}

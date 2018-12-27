package com.passport.main.controller;

import com.common.annotation.RoleResource;
import com.common.util.BeanCoper;
import com.common.util.DateUtil;
import com.common.util.StringUtils;
import com.passport.domain.ProxyInfo;
import com.passport.main.controller.dto.IdChangePassDto;
import com.passport.service.ClientUserInfoService;
import com.passport.service.ProxyInfoService;
import com.passport.main.AbstractClientController;
import com.passport.main.controller.dto.ProxyDto;
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
        return buildMessage(() ->{
            ProxyInfo byId = proxyInfoService.findById(getIdByJson(content));
            return byId;
        } );
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
            entity.setName(info.getName());
            entity.setPhone(info.getPhone());
            entity.setGames(info.getGames());
            entity.setLinkMan(info.getLinkMan());
            entity.setToken(info.getToken());
            entity.setEndTime(DateUtil.parseDate(info.getEndTime()));
            entity.setEncodingKey(info.getEncodingKey());
            entity.setDomain(info.getDomain());
            entity.setRemark(info.getRemark());
            entity.setStatus(info.getStatus());
            entity.setId(info.getId());
            return proxyInfoService.save(entity);
        });
    }

//    @RoleResource(resource = "passport")
//    @RequestMapping("/proxy/changePass")
//    public Map<String, Object> changePass(@RequestBody IdChangePassDto dto) {
//        return buildMessage(() -> {
//            proxyInfoService.changePass(dto.getId(), dto.getPassword());
//            return null;
//        });
//    }
}

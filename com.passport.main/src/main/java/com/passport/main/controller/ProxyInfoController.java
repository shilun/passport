package com.passport.main.controller;

import com.common.annotation.RoleResource;
import com.common.util.BeanCoper;
import com.common.util.DateUtil;
import com.common.util.StringUtils;
import com.passport.domain.ProxyInfo;
import com.passport.main.controller.dto.IdChangePassDto;
import com.passport.rpc.dto.ProxyDto;
import com.passport.service.ClientUserInfoService;
import com.passport.service.ProxyInfoService;
import com.passport.main.AbstractClientController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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
     * @return
     */
    @RoleResource(resource = "passport")
    @RequestMapping("/proxy/view")
    public Map<String, Object> view(@RequestBody Map<String, String> params) {
        return buildMessage(() -> {
            ProxyInfo byId = proxyInfoService.findById(params.get("id"));
            return byId;
        });
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
            List<String> domains = new ArrayList();
            for (String domain : info.getDomain()) {
                if (StringUtils.isNotEmpty(domain)) {
                    domains.add(domain);
                }
            }
            entity.setDomain(domains.toArray(new String[domains.size()]));
            entity.setRemark(info.getRemark());
            entity.setStatus(info.getStatus());
            entity.setId(info.getId());
            //新加代理商配置
            entity.setCpRate(info.getCpRate());
            entity.setQpRate(info.getQpRate());
            entity.setServiceMoney(info.getServiceMoney());
            proxyInfoService.save(entity);
            return null;
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

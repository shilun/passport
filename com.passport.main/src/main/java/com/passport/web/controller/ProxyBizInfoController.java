package com.passport.web.controller;

import com.common.annotation.RoleResource;
import com.common.exception.BizException;
import com.common.util.BeanCoper;
import com.passport.domain.ProxyBizInfo;
import com.passport.domain.ProxyInfo;
import com.passport.service.ProxyBizInfoService;
import com.passport.service.ProxyInfoService;
import com.passport.service.constant.CodeConstant;
import com.passport.service.constant.MessageConstant;
import com.passport.web.AbstractClientController;
import com.passport.web.controller.dto.ProxyBizInfoDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping(method = {RequestMethod.POST})
public class ProxyBizInfoController extends AbstractClientController {
    @Resource
    private ProxyInfoService proxyInfoService;
    @Resource
    private ProxyBizInfoService bizInfoService;

    /**
     * 查询
     *
     * @param info
     * @return
     */
    @RoleResource(resource = "passport")
    @RequestMapping("/proxyBiz/list")
    public Map<String, Object> list(@RequestBody ProxyBizInfoDto info) {
        return buildMessage(() -> {
            ProxyBizInfo entity = new ProxyBizInfo();
            BeanCoper.copyProperties(entity, info);
            return bizInfoService.queryByPage(entity, info.getPageinfo().getPage());
        });
    }

    /**
     * 查询
     *
     * @param content
     * @return
     */
    @RoleResource(resource = "passport")
    @RequestMapping("/proxyBiz/view")
    public Map<String, Object> view(@RequestBody String content) {
        return buildMessage(() -> bizInfoService.findById(getIdByJson(content)));
    }

    /**
     * 保存
     *
     * @param info
     * @return
     */
    @RequestMapping("/proxyBiz/save")
    @RoleResource(resource = "passport")
    public Map<String, Object> save(@RequestBody ProxyBizInfoDto info) {
        return buildMessage(() -> {
            Long proxyId = info.getProxyId();
            if (proxyId == null || proxyId == 0) {
                throw new BizException(CodeConstant.PARAM_NULL, MessageConstant.PARAM_NULL);
            }
            ProxyInfo proxyInfo = proxyInfoService.findById(proxyId);
            if (proxyInfo == null) {
                throw new BizException(CodeConstant.USER_NULL, "对应代理不存在");
            }
            boolean isNew = info.getId() == null || info.getId().equals(0L);
            if (isNew) {
                Integer[] games = proxyInfo.getGames();
                Integer bizType = info.getBizType();
                if (games != null) {
                    boolean contains = false;
                    for (int i = 0; i < games.length; i++) {
                        if (games[i].equals(bizType)) {
                            contains = true;
                        }
                    }
                    if (!contains) {
                        Integer[] newGames = new Integer[games.length + 1];
                        System.arraycopy(games, 0, newGames, 0, games.length);
                        newGames[games.length] = bizType;
                        games = newGames;
                    } else {
                        throw new BizException("biz_type_repeat", "代理游戏重复");
                    }
                } else {
                    games = new Integer[]{info.getBizType()};
                }
                proxyInfo.setGames(games);
                proxyInfoService.save(proxyInfo);
            }
            ProxyBizInfo bizInfo=new ProxyBizInfo();
            BeanCoper.copyProperties(bizInfo,info);
            bizInfoService.save(bizInfo);
            return null;
        });
    }
}

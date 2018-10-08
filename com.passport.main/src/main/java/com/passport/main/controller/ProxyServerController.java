package com.passport.main.controller;

import com.common.annotation.RoleResource;
import com.common.exception.BizException;
import com.common.util.BeanCoper;
import com.passport.domain.ProxyServerInfo;
import com.passport.main.AbstractClientController;
import com.passport.main.controller.dto.ProxyServerAddDto;
import com.passport.main.controller.dto.ProxyServerChangeDto;
import com.passport.main.controller.dto.ProxyServerQueryDto;
import com.passport.service.ProxyServerInfoService;
import com.passport.service.util.Tool;
import io.swagger.annotations.Api;
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
     * 添加配置
     * @param dto
     * @return
     */
    @RoleResource(resource = "passport")
    @RequestMapping("/server/add")
    public Map<String, Object> add(@RequestBody ProxyServerAddDto dto) {
        return buildMessage(() -> {
            proxyServerInfoService.addServerInfo(dto.getGameType(),dto.getProxyId(),dto.getIp(),dto.getPort(),dto.getEvironment(),dto.getServerId(),dto.getIsClose());
            return null;
        });
    }

    /**
     * 修改配置
     * @param dto
     * @return
     */
    @RoleResource(resource = "passport")
    @RequestMapping("/server/change")
    public Map<String, Object> change(@RequestBody ProxyServerChangeDto dto) {
        return buildMessage(() -> {
            String value = dto.getValue();
            Long id = dto.getId();
            switch (dto.getChangeEnum()){
                case Ip:
                    proxyServerInfoService.changeIp(id,value);
                    break;
                case Port:
                    if(!tool.isNo(value)){
                        throw new BizException("端口错误");
                    }
                    proxyServerInfoService.changePort(id,Integer.parseInt(value));
                    break;
                case Evironment:
                    proxyServerInfoService.changeEvironment(id,value);
                    break;
                case Close:
                    if(!tool.isNo(value)){
                        throw new BizException("关闭类型错误");
                    }
                    proxyServerInfoService.close(id,Integer.parseInt(value));
                    break;
                    default:throw new BizException("修改配置类型错误");
            }
            return null;
        });
    }

    /**
     * 获取配置
     * @param dto
     * @return
     */
    @RoleResource(resource = "passport")
    @RequestMapping("/server/queryProxyServers")
    public Map<String, Object> queryProxyServers(@RequestBody ProxyServerQueryDto dto) {
        return buildMessage(() -> {
            ProxyServerInfo info = new ProxyServerInfo();
            BeanCoper.copyProperties(info,dto);
            return proxyServerInfoService.queryByPage(info,dto.getPageinfo().getPage());
        });
    }
}

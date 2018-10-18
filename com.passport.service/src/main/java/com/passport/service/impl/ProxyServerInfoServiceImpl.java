package com.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.passport.domain.ProxyServerInfo;
import com.passport.service.ProxyServerInfoService;
import org.springframework.stereotype.Service;

/**
 * @author Luo
 * @date 2018/10/8 9:58
 */
@Service
public class ProxyServerInfoServiceImpl extends AbstractMongoService<ProxyServerInfo> implements ProxyServerInfoService {
    @Override
    protected Class getEntityClass() {
        return ProxyServerInfo.class;
    }

    @Override
    public Long addServerInfo(Integer gameType, Long proxyId, String ip, Integer port, String evironment, String serverId, Integer isClose) {
        ProxyServerInfo info = new ProxyServerInfo();
        info.setGameType(gameType);
        info.setProxyId(proxyId);
        info.setIp(ip);
        info.setPort(port);
        info.setEvironment(evironment);
        info.setServerId(serverId);
        return this.insert(info);
    }

    @Override
    public void changeIp(Long id,String newIp) {
        ProxyServerInfo info = new ProxyServerInfo();
        info.setId(id);
        info.setIp(newIp);
        this.up(info);
    }

    @Override
    public void changePort(Long id,Integer port) {
        ProxyServerInfo info = new ProxyServerInfo();
        info.setId(id);
        info.setPort(port);
        this.up(info);

    }

    @Override
    public void changeEvironment(Long id,String evironment) {
        ProxyServerInfo info = new ProxyServerInfo();
        info.setId(id);
        info.setEvironment(evironment);
        this.up(info);

    }

    @Override
    public void close(Long id,Integer isClose) {
        ProxyServerInfo info = new ProxyServerInfo();
        info.setId(id);
        this.up(info);
    }
}

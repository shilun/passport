package com.passport.service;

import com.common.mongo.MongoService;
import com.passport.domain.ProxyServerInfo;

/**
 * @author Luo
 * @date 2018/10/8 9:49
 */
public interface ProxyServerInfoService extends MongoService<ProxyServerInfo> {

    /**
     * 添加配置
     * @param gameType
     * @param proxyId
     * @param ip
     * @param port
     * @param evironment
     * @param serverId
     * @param isClose
     */
    Long addServerInfo(Integer gameType,Long proxyId,String ip,Integer port,String evironment,String serverId,Integer isClose);

    /**
     * 改变ip
     * @param newIp
     */
    void changeIp(Long id,String newIp);

    /**
     * 改变port
     * @param port
     */
    void changePort(Long id,Integer port);

    /**
     * 改变环境
     * @param evironment
     */
    void changeEvironment(Long id,String evironment);

    /**
     * 关闭
     * @param isClose  1.否  2.是
     */
    void close(Long id,Integer isClose);

}

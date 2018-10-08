package com.passport.main.controller.dto;

import com.common.util.AbstractDTO;

/**
 * @author Luo
 * @date 2018/10/8 10:48
 */
public class ProxyServerQueryDto extends AbstractDTO {
    private static final long serialVersionUID = 4418727978249318617L;
    /**
     * 1 棋牌 2 彩票 3 小游戏
     */
    private Integer gameType;
    /**
     * 代理id
     */
    private Long proxyId;
    /**
     * ip或域名
     */
    private String ip;
    /**
     * port
     */
    private Integer port;
    /**
     * development
     */
    private String evironment;
    private String serverId;
    /**
     * 是否关闭  1.否  2.是
     */
    private Integer isClose;

    public Integer getGameType() {
        return gameType;
    }

    public void setGameType(Integer gameType) {
        this.gameType = gameType;
    }

    public Long getProxyId() {
        return proxyId;
    }

    public void setProxyId(Long proxyId) {
        this.proxyId = proxyId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getEvironment() {
        return evironment;
    }

    public void setEvironment(String evironment) {
        this.evironment = evironment;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public Integer getIsClose() {
        return isClose;
    }

    public void setIsClose(Integer isClose) {
        this.isClose = isClose;
    }
}

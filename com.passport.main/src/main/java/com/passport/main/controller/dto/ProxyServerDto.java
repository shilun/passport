package com.passport.main.controller.dto;

import com.common.util.AbstractDTO;

/**
 * @Author: CSL
 * @Date: 2018/10/18 14:28
 */
public class ProxyServerDto extends AbstractDTO {

    /**
     * 1 棋牌 2 彩票 3 小游戏
     */
    private Integer gameType;
    /**
     * 游戏ID
     */
    private Integer gameId;
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
    /**
     * serverId
     */
    private String serverId;
    /**
     * 是否关闭  1.否  2.是
     */
    private Integer status;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

}

package com.passport.main.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/10/8 10:13
 */
@ApiModel(description = "添加服务器配置")
public class ProxyServerAddDto implements Serializable {
    private static final long serialVersionUID = 387048547609862826L;
    @ApiModelProperty(value = "1 棋牌 2 彩票 3 小游戏")
    private Integer gameType;
    @ApiModelProperty(value = "代理id")
    private Long proxyId;
    @ApiModelProperty(value = "ip或域名")
    private String ip;
    @ApiModelProperty(value = "port")
    private Integer port;
    @ApiModelProperty(value = "evironment")
    private String evironment;
    private String serverId;
    @ApiModelProperty(value = "是否关闭  1.否  2.是")
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

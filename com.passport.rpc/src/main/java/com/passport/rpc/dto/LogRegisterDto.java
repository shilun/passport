package com.passport.rpc.dto;

import com.common.annotation.QueryField;
import com.common.mongo.QueryType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Luo
 * @date 2018/9/22 15:07
 */
public class LogRegisterDto implements Serializable {
    private static final long serialVersionUID = -8899999351295338575L;
    /**
     * 用户pin
     */
    private String pin;

    /**
     * 代理商id
     */
    private Long proxyId;
    /**
     * 代理商id
     */
    private String ip;
    /**
     * 注册时间
     */
    private Date registerDate;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
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

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }
}

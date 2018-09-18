package com.passport.rpc.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Luo
 * @date 2018/9/18 15:32
 */
public class LogLoginDto implements Serializable {
    private static final long serialVersionUID = -2539034226167580338L;
    /**
     * 用户pin
     */
    private String pin;

    /**
     * 代理商id
     */
    private Long proxyId;

    private Date loginDay;

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

    public Date getLoginDay() {
        return loginDay;
    }

    public void setLoginDay(Date loginDay) {
        this.loginDay = loginDay;
    }
}

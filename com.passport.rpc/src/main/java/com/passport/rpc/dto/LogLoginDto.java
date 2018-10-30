package com.passport.rpc.dto;

import com.common.util.AbstractDTO;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Luo
 * @date 2018/9/18 15:32
 */
public class LogLoginDto extends AbstractDTO implements Serializable {
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

    private Date registerDate;

    private String ip;

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

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}

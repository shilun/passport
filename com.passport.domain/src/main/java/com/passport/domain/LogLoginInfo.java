package com.passport.domain;

import com.common.annotation.QueryField;
import com.common.mongo.QueryType;
import com.common.util.AbstractBaseEntity;
import org.springframework.data.annotation.Transient;

import java.util.Date;

/**
 * @author Luo
 * @date 2018/9/18 15:42
 */
public class LogLoginInfo extends AbstractBaseEntity {
    /**
     * 用户pin
     */
    private String pin;

    /**
     * 代理商id
     */
    private Long proxyId;

    /**
     * 登陆时间
     */
    private Date loginDay;

    /**
     * 注册时间
     */
    private Date registerDate;

    @Transient
    @QueryField(name="loginDay",type= QueryType.GTE)
    private Date loginStartTime;

    @Transient
    @QueryField(name="loginDay",type= QueryType.LTE)
    private Date loginEndTime;

    @Transient
    @QueryField(name="registerDate",type= QueryType.GTE)
    private Date regStartTime;

    @Transient
    @QueryField(name="registerDate",type= QueryType.LTE)
    private Date regEndTime;

    private Long userCode;

    /**
     * 登陆ip
     */
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

    public void setLoginStartTime(Date loginStartTime) {
        this.loginStartTime = loginStartTime;
    }

    public void setLoginEndTime(Date loginEndTime) {
        this.loginEndTime = loginEndTime;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public void setRegStartTime(Date regStartTime) {
        this.regStartTime = regStartTime;
    }

    public void setRegEndTime(Date regEndTime) {
        this.regEndTime = regEndTime;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getLoginStartTime() {
        return loginStartTime;
    }

    public Date getLoginEndTime() {
        return loginEndTime;
    }

    public Date getRegStartTime() {
        return regStartTime;
    }

    public Date getRegEndTime() {
        return regEndTime;
    }

    public String getIp() {
        return ip;
    }

    public Long getUserCode() {
        return userCode;
    }

    public void setUserCode(Long userCode) {
        this.userCode = userCode;
    }
}

package com.passport.domain;

import com.common.annotation.QueryField;
import com.common.mongo.QueryType;
import com.common.util.AbstractBaseEntity;
import org.springframework.data.annotation.Transient;

import java.util.Date;

/**
 * @author Luo
 * @date 2018/9/22 14:58
 */
public class LogRegisterInfo extends AbstractBaseEntity {
    private static final long serialVersionUID = -1163451708669130354L;
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

    @Transient
    @QueryField(name="registerDate",type= QueryType.GTE)
    private Date regStartTime;

    @Transient
    @QueryField(name="registerDate",type= QueryType.LTE)
    private Date regEndTime;

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

    public Date getRegStartTime() {
        return regStartTime;
    }

    public void setRegStartTime(Date regStartTime) {
        this.regStartTime = regStartTime;
    }

    public Date getRegEndTime() {
        return regEndTime;
    }

    public void setRegEndTime(Date regEndTime) {
        this.regEndTime = regEndTime;
    }
}

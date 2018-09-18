package com.passport.domain;

import com.common.annotation.QueryField;
import com.common.mongo.QueryType;
import com.common.util.AbstractBaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.passport.domain.serializer.DateJsonSerializer;
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

    @JsonSerialize(using = DateJsonSerializer.class)
    private Date loginDay;

    @Transient
    @QueryField(name="loginDay",type= QueryType.GTE)
    private Date startTime;

    @Transient
    @QueryField(name="loginDay",type= QueryType.LTE)
    private Date endTime;

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

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}

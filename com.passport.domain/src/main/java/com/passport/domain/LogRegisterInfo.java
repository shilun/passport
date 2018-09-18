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
 * @date 2018/9/18 17:14
 */
public class LogRegisterInfo extends AbstractBaseEntity {
    private static final long serialVersionUID = 940885524286271055L;

    /**
     * 用户pin
     */
    private String pin;

    /**
     * 代理商id
     */
    private Long proxyId;

    @JsonSerialize(using = DateJsonSerializer.class)
    private Date registerDay;

    @Transient
    @QueryField(name="registerDay",type= QueryType.GTE)
    private Date startTime;

    @Transient
    @QueryField(name="registerDay",type= QueryType.LTE)
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

    public Date getRegisterDay() {
        return registerDay;
    }

    public void setRegisterDay(Date registerDay) {
        this.registerDay = registerDay;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}

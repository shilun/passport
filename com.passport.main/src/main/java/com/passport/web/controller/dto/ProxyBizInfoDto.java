package com.passport.web.controller.dto;

import com.common.util.AbstractDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.passport.domain.serializer.DateJsonDeserializer;

import java.util.Date;

/**
 * 业务信息
 */
public class ProxyBizInfoDto extends AbstractDTO {

    private String pin;
    /**
     * 代理商id
     */
    private Long proxyId;
    /** 1 棋牌 2 彩票 3 小游戏
     * 申请业务
     */
    private Integer bizType;
    /**
     * 开始时间
     */
    @JsonDeserialize(using = DateJsonDeserializer.class)
    private Date startTime;
    /**
     * 结束时间
     */
    @JsonDeserialize(using = DateJsonDeserializer.class)
    private Date endTime;

    /**
     * 1 启用 2 停用
     */
    private Integer status;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getProxyId() {
        return proxyId;
    }

    public void setProxyId(Long proxyId) {
        this.proxyId = proxyId;
    }

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
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

package com.passport.domain;

import com.common.util.AbstractBaseEntity;

import java.util.Date;

/**
 * 业务信息
 */
public class ProxyBizInfo extends AbstractBaseEntity {
    /**
     * 代理商id
     */
    private Long proxyId;
    /**
     * 申请业务
     */
    private Integer bizType;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;

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

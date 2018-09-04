package com.passport.rpc.dto;

import com.common.util.AbstractBaseEntity;

import java.util.Date;

/**
 * 业务信息
 */
public class ProxyBizInfo  {

    /** 1 棋牌 2 彩票 3 小游戏
     * 申请业务
     */
    private String bizType;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 1 启用 2 停用
     */
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }
}

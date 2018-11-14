package com.passport.domain;

import com.common.annotation.QueryField;
import com.common.mongo.QueryType;
import com.common.util.AbstractBaseEntity;
import org.springframework.data.annotation.Transient;

import java.util.Date;

/**
 * 用户限制信息
 * @author Luo
 * @date 2018/10/22 19:03
 */

public class LimitInfo extends AbstractBaseEntity {
    private static final long serialVersionUID = 3715315246916271897L;
    /**
     * 后台设置单ip限制数量的一个名称
     */
    private String name;
    /**
     *后台设置单ip限制数量,根据name来查询这个数量
     */
    private Integer allNum;

    /**
     * 代理id
     */
    private Long proxyId;
    /**
     * 对pin进行限制
     */
    private String pin;
    /**
     * 对ip进行限制
     */
    private String ip;
    /**
     * 限制类型   1.登陆  2.注册
     */
    private Integer limitType;
    /**
     * 开始限制时间
     */
    private Date limitStartTime;
    /**
     * 结束限制时间
     */
    private Date limitEndTime;
    /**
     * 当前ip已经注册的数量
     */
    private Integer currentIpRegNum;
    @Transient
    @QueryField(name = "currentIpRegNum", type = QueryType.GTE)
    private Integer startCurrentIpRegNum;
    @QueryField(name = "currentIpRegNum", type = QueryType.LTE)
    @Transient
    private Integer endCurrentIpRegNum;
    /**
     * 备注
     */
    private String remarks;

    private Long userCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAllNum() {
        return allNum;
    }

    public void setAllNum(Integer allNum) {
        this.allNum = allNum;
    }

    public Long getProxyId() {
        return proxyId;
    }

    public void setProxyId(Long proxyId) {
        this.proxyId = proxyId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getLimitType() {
        return limitType;
    }

    public void setLimitType(Integer limitType) {
        this.limitType = limitType;
    }

    public Date getLimitStartTime() {
        return limitStartTime;
    }

    public void setLimitStartTime(Date limitStartTime) {
        this.limitStartTime = limitStartTime;
    }

    public Date getLimitEndTime() {
        return limitEndTime;
    }

    public void setLimitEndTime(Date limitEndTime) {
        this.limitEndTime = limitEndTime;
    }

    public Integer getCurrentIpRegNum() {
        return currentIpRegNum;
    }

    public void setCurrentIpRegNum(Integer currentIpRegNum) {
        this.currentIpRegNum = currentIpRegNum;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getStartCurrentIpRegNum() {
        return startCurrentIpRegNum;
    }

    public void setStartCurrentIpRegNum(Integer startCurrentIpRegNum) {
        this.startCurrentIpRegNum = startCurrentIpRegNum;
    }

    public Integer getEndCurrentIpRegNum() {
        return endCurrentIpRegNum;
    }

    public void setEndCurrentIpRegNum(Integer endCurrentIpRegNum) {
        this.endCurrentIpRegNum = endCurrentIpRegNum;
    }

    public Long getUserCode() {
        return userCode;
    }

    public void setUserCode(Long userCode) {
        this.userCode = userCode;
    }
}

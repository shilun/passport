package com.passport.rpc.dto;

import com.common.util.AbstractDTO;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Luo
 * @date 2018/10/22 19:20
 */
public class LimitDto extends AbstractDTO implements Serializable {
    private static final long serialVersionUID = -7966085003787009534L;
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
    /**
     * 备注
     */
    private String remarks;

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

    @Override
    public String toString() {
        return "LimitDto{" +
                "name='" + name + '\'' +
                ", allNum=" + allNum +
                ", proxyId=" + proxyId +
                ", pin='" + pin + '\'' +
                ", ip='" + ip + '\'' +
                ", limitType=" + limitType +
                ", limitStartTime=" + limitStartTime +
                ", limitEndTime=" + limitEndTime +
                ", currentIpRegNum=" + currentIpRegNum +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}

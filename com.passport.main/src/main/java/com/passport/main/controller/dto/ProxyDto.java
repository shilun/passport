package com.passport.main.controller.dto;

import com.common.util.AbstractDTO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 代理商信息
 */
public class ProxyDto  extends AbstractDTO {
    /**
     * 用户pin
     */
    private String pin;

    /**
     *
     */
    private Integer[] games;

    /**
     * 公司名称
     */
    private String name;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 联系人
     */
    private String linkMan;

    /**
     * 接入token
     */
    private String token;

    /**
     * 结整时间
     */
    private String endTime;

    /**
     * 接入加密key uuid生成
     */
    private String encodingKey;
    /**
     * 网站
     */
    private String[] domain;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态
     */
    private Integer status;

    /**
     * 彩票费率
     */
    private BigDecimal cpRate;
    /**
     * 棋牌费率
     */
    private BigDecimal qpRate;
    /**
     * 服务费
     */
    private BigDecimal serviceMoney;

    public BigDecimal getCpRate() {
        return cpRate;
    }

    public void setCpRate(BigDecimal cpRate) {
        this.cpRate = cpRate;
    }

    public BigDecimal getQpRate() {
        return qpRate;
    }

    public void setQpRate(BigDecimal qpRate) {
        this.qpRate = qpRate;
    }

    public BigDecimal getServiceMoney() {
        return serviceMoney;
    }

    public void setServiceMoney(BigDecimal serviceMoney) {
        this.serviceMoney = serviceMoney;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }



    public Integer[] getGames() {
        return games;
    }

    public void setGames(Integer[] games) {
        this.games = games;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEncodingKey() {
        return encodingKey;
    }

    public void setEncodingKey(String encodingKey) {
        this.encodingKey = encodingKey;
    }

    public String[] getDomain() {
        return domain;
    }

    public void setDomain(String[] domain) {
        this.domain = domain;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

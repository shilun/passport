package com.passport.main.controller.dto;

import com.common.util.AbstractDTO;

public class ProxyUserDto  extends AbstractDTO {
    /**
     * 代理商
     */
    private Long proxyId;
    /**
     * 账户
     */
    private String pin;
    /**
     * 电话
     */
    private String phone;
    /**
     * 备注
     */
    private String desc;
    //1 运营 2 财务 3 配置
    private Long[] roles;


    /**
     * 用户状态
     */
    private Integer status;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long[] getRoles() {
        return roles;
    }

    public void setRoles(Long[] roles) {
        this.roles = roles;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

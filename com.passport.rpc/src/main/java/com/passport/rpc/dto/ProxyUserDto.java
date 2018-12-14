package com.passport.rpc.dto;

import java.io.Serializable;

public class ProxyUserDto  implements Serializable {
    private String loginToken;
    private String account;
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
     * 密码
     */
    private String pass;

    /**
     * 用户状态
     */
    private Integer status;

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

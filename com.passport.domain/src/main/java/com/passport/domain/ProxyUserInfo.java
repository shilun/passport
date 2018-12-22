package com.passport.domain;

import com.common.util.AbstractBaseEntity;
import org.springframework.data.annotation.Transient;

/**
 * 代理商用户
 */
public class ProxyUserInfo extends AbstractBaseEntity {

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

    public String getDesc() {
        return desc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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

    public Long[] getRoles() {
        return roles;
    }

    public void setRoles(Long[] roles) {
        this.roles = roles;
    }
}

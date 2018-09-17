package com.passport.main.controller.dto;

import com.common.util.AbstractDTO;

import java.io.Serializable;

public class AdminDto extends AbstractDTO implements Serializable {
    /**pin*/
    private String pin;
    /**妮称*/
    private String name;
    /**状态*/
    private Integer status;
    /**电话*/
    private String phone;
    /**邮件*/
    private String email;
    /**性别*/
    private Integer sexType;
    /**角色*/
    private Long[] roles;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSexType() {
        return sexType;
    }

    public void setSexType(Integer sexType) {
        this.sexType = sexType;
    }

    public Long[] getRoles() {
        return roles;
    }

    public void setRoles(Long[] roles) {
        this.roles = roles;
    }
}

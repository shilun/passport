package com.passport.domain;

import com.common.util.AbstractBaseEntity;

import java.util.Date;

/**
 * 客户用户信息
 */
public class ClientUserInfo extends AbstractBaseEntity {
    /**
     * 用户pin
     */
    private String pin;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 电话
     */
    private String phone;
    /**
     * 邮件
     */
    private String email;
    /**密码*/
    private String passwd;
    /**
     * 性别
     */
    private Integer sexType;
    /**
     * 用户状态
     */
    private Integer status;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public Integer getSexType() {
        return sexType;
    }

    public void setSexType(Integer sexType) {
        this.sexType = sexType;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

package com.shilun.passport.domain;

import com.common.util.AbstractBaseEntity;

import java.util.Date;

/**
 * 管理员用户信息
 */
public class AdminUserInfo extends AbstractBaseEntity {
    /**pin*/
    private String pin;
    /**妮称*/
    private String nickName;
    /**密码*/
    private String passwd;
    /**状态*/
    private Integer status;
    /**电话*/
    private String phone;
    /**邮件*/
    private String email;
    /**真实姓名*/
    private String realName;
    /**生日*/
    private Date birthday;
    /**性别*/
    private Integer sexType;
    /**入职时间*/
    private Date inCompanyDate;

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

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getSexType() {
        return sexType;
    }

    public void setSexType(Integer sexType) {
        this.sexType = sexType;
    }

    public Date getInCompanyDate() {
        return inCompanyDate;
    }

    public void setInCompanyDate(Date inCompanyDate) {
        this.inCompanyDate = inCompanyDate;
    }
}

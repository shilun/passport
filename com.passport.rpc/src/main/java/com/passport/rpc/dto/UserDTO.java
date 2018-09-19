package com.passport.rpc.dto;

import com.common.util.AbstractDTO;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shilun on 16-12-5.
 */
public class UserDTO extends AbstractDTO implements Serializable {
    private static final long serialVersionUID = 8642175623513171274L;
    private String email;
    private String pin;
    private String nickName;
    private String mobile;
    private String birthday;
    private Integer sexType;
    private Boolean initPass;
    private String token;
    private Long proxyId;
    private Date birthDay;
    private Long id;
    private String realName;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getSexType() {
        return sexType;
    }

    public void setSexType(Integer sexType) {
        this.sexType = sexType;
    }

    public Boolean getInitPass() {
        return initPass;
    }

    public void setInitPass(Boolean initPass) {
        this.initPass = initPass;
    }

    public Long getProxyId() {
        return proxyId;
    }

    public void setProxyId(Long proxyId) {
        this.proxyId = proxyId;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

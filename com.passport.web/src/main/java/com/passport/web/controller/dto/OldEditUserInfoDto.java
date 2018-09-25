package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/9/25 14:31
 */
@ApiModel(description = "编辑用户信息")
public class OldEditUserInfoDto implements Serializable {
    private static final long serialVersionUID = -1745267016685487336L;
    private Integer userId;
    private String nick;
    private Long qq;
    private String wechat;
    private Integer gender;
    private String sign;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Long getQq() {
        return qq;
    }

    public void setQq(Long qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}

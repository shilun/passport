package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Luo
 * @date 2018/8/31 18:05
 */
public class ForgetPassVerDto{
    private static final long serialVersionUID = 885207316961913603L;
    /**
     * 账户
     */
    private String account;
    @ApiModelProperty(value = "验证码")
    private String code;
    @ApiModelProperty(value = "新密码")
    private String pass;

    public String getCode() {
        return code;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}

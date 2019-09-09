package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/8/31 13:49
 */
@ApiModel(description = "验证码登录表单")
public class LoginByCodeDto implements Serializable {
    private static final long serialVersionUID = 8412647014801512649L;
    @ApiModelProperty(value = "账号")
    private String account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}

package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/9/21 16:58
 */
@ApiModel(description = "注册表单")
public class OldRegDto implements Serializable {
    private static final long serialVersionUID = 3929945176595567524L;
    @ApiModelProperty(value = "手机号")
    private String account;
    @ApiModelProperty(value = "密码")
    private String pass;
    @ApiModelProperty(value = "验证码")
    private String validateCode;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }
}

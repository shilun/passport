package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/9/22 11:07
 */
@ApiModel(description = "忘记密码")
public class OldForgetPwd implements Serializable {
    private static final long serialVersionUID = -262909021291628466L;
    @ApiModelProperty(value = "账号")
    private String accessName;
    @ApiModelProperty(value = "密码")
    private String pwd;
    @ApiModelProperty(value = "验证码")
    private String validateCode;

    public String getAccessName() {
        return accessName;
    }

    public void setAccessName(String accessName) {
        this.accessName = accessName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }
}

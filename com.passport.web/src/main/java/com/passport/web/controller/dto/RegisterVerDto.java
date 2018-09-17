package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Luo
 * @date 2018/8/31 14:06
 */
public class RegisterVerDto extends RegisterDto{
    private static final long serialVersionUID = 1688043110049698697L;
    @ApiModelProperty(value = "验证码")
    private String code;
    @ApiModelProperty(value = "密码")
    private String pass;

    public String getCode() {
        return code;
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

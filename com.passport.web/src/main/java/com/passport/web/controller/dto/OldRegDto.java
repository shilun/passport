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
    private String accessName;
    @ApiModelProperty(value = "密码")
    private String accessToken;
    @ApiModelProperty(value = "验证码")
    private String validateCode;
    @ApiModelProperty(value = "登陆方式")
    private Integer loginType;

    public String getAccessName() {
        return accessName;
    }

    public void setAccessName(String accessName) {
        this.accessName = accessName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public Integer getLoginType() {
        return loginType;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }
}

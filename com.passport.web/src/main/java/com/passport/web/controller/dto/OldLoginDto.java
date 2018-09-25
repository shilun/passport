package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/9/22 10:21
 */
@ApiModel(description = "登陆表单")
public class OldLoginDto implements Serializable {
    private static final long serialVersionUID = 5448012990455746359L;
    @ApiModelProperty(value = "登陆方式")
    private Integer type;
    @ApiModelProperty(value = "账号")
    private String loginName;
    @ApiModelProperty(value = "密码")
    private String pwd;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}

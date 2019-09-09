package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/9/22 11:03
 */
@ApiModel(description = "修改登陆密码")
public class OldUpdatePwdDto implements Serializable {
    private static final long serialVersionUID = 7001477580695569239L;
    @ApiModelProperty(value = "旧密码")
    private String pwd;
    @ApiModelProperty(value = "新密码")
    private String newPwd;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}

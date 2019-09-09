package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/8/31 10:11
 */
@ApiModel(description = "注册表单")
public class RegisterDto implements Serializable {
    private static final long serialVersionUID = -7986983949114880681L;
    @ApiModelProperty(value = "账号")
    private String account;


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}

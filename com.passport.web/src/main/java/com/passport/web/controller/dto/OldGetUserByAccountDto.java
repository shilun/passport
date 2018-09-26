package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/9/25 14:25
 */
@ApiModel(description = "获取验证码")
public class OldGetUserByAccountDto implements Serializable {
    private static final long serialVersionUID = -4461961812160558123L;
    @ApiModelProperty(value = "账号")
    private String accessName;

    public String getAccessName() {
        return accessName;
    }

    public void setAccessName(String accessName) {
        this.accessName = accessName;
    }
}

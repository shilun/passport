package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/9/25 14:10
 */
@ApiModel(description = "获取用户")
public class OldGetUserByIdDto implements Serializable {
    private static final long serialVersionUID = 1334578772178555738L;
    private Integer userCode;

    public Integer getUserCode() {
        return userCode;
    }

    public void setUserCode(Integer userCode) {
        this.userCode = userCode;
    }
}

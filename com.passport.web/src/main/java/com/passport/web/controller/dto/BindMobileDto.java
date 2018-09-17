package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/8/31 17:31
 */
public class BindMobileDto implements Serializable {
    private static final long serialVersionUID = -4648134696314381108L;
    @ApiModelProperty(value = "手机号")
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

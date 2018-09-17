package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/8/31 16:38
 */
public class ChangeMobileDto implements Serializable {
    private static final long serialVersionUID = 5920026290893877357L;
    @ApiModelProperty(value = "手机号")
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

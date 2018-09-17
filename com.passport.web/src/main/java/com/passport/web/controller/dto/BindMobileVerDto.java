package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Luo
 * @date 2018/8/31 17:33
 */
public class BindMobileVerDto extends BindMobileDto{
    private static final long serialVersionUID = 3581944920725392012L;

    @ApiModelProperty(value = "验证码")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Luo
 * @date 2018/8/31 16:40
 */
public class ChangeMobileVerDto extends ChangeMobileDto {
    private static final long serialVersionUID = 1621993199627930024L;
    @ApiModelProperty(value = "验证码")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

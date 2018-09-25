package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/9/21 17:02
 */
@ApiModel(description = "获取验证码")
public class OldGetCodeDto implements Serializable {
    private static final long serialVersionUID = 8946329497408251943L;
    @ApiModelProperty(value = "手机号")
    private String phoneNo;
    @ApiModelProperty(value = "验证码类型")
    private String codeType;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }
}

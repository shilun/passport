package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/11/16 9:52
 */
public class OldBachRegDto implements Serializable {
    private static final long serialVersionUID = 6036502096705220438L;
    @ApiModelProperty(value = "起始手机号")
    private Long beginPhone;
    @ApiModelProperty(value = "结束手机号")
    private Long endPhone;
    @ApiModelProperty(value = "密码")
    private String pass;

    public Long getBeginPhone() {
        return beginPhone;
    }

    public void setBeginPhone(Long beginPhone) {
        this.beginPhone = beginPhone;
    }

    public Long getEndPhone() {
        return endPhone;
    }

    public void setEndPhone(Long endPhone) {
        this.endPhone = endPhone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}

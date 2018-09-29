package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/9/25 15:41
 */
@ApiModel(description = "二维码注册")
public class OldQRRegDto implements Serializable {
    private static final long serialVersionUID = 8894215864404639400L;
    @ApiModelProperty(value = "推荐人id")
    private Long recommendId;
    @ApiModelProperty(value = "手机号")
    private String accessName;
    @ApiModelProperty(value = "密码")
    private String accessToken;
    @ApiModelProperty(value = "验证码")
    private String validateCode;
    @ApiModelProperty(value = "登陆方式")
    private Integer loginType;

    public Long getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(Long recommendId) {
        this.recommendId = recommendId;
    }

    public String getAccessName() {
        return accessName;
    }

    public void setAccessName(String accessName) {
        this.accessName = accessName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public Integer getLoginType() {
        return loginType;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }
}

package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/9/4 9:09
 */
@ApiModel(description = "添加用户扩展信息表单")
public class UserExtendDto implements Serializable {
    private static final long serialVersionUID = -6245218881162036853L;
    @ApiModelProperty(value = "用户短码")
    private Integer userCode;
    @ApiModelProperty(value = "头像地址")
    private String headUrl;
    @ApiModelProperty(value = "是否是机器人")
    private Integer isRobot;
    @ApiModelProperty(value = "签名")
    private String sign;
    @ApiModelProperty(value = "微信")
    private String wechat;
    @ApiModelProperty(value = "身份证号")
    private String idCard;
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    public Integer getUserCode() {
        return userCode;
    }

    public void setUserCode(Integer userCode) {
        this.userCode = userCode;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public Integer getIsRobot() {
        return isRobot;
    }

    public void setIsRobot(Integer isRobot) {
        this.isRobot = isRobot;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}

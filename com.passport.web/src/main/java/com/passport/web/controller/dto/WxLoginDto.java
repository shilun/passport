package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/12/14 13:43
 */
@ApiModel(description = "微信登陆")
public class WxLoginDto implements Serializable {
    private static final long serialVersionUID = -2204476585083230238L;
    @ApiModelProperty(value = "微信id")
    private String wxId;
    @ApiModelProperty(value = "昵称")
    private String nick;
    @ApiModelProperty(value = "头像")
    private String headImg;
    @ApiModelProperty(value = "性别")
    private Integer sex;

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}

package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/8/31 17:59
 */
public class ChangeNickDto implements Serializable {
    private static final long serialVersionUID = -4643721406149263511L;
    @ApiModelProperty(value = "新昵称")
    private String nick;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}

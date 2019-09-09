package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/8/31 17:37
 */
public class ChangePassDto implements Serializable {
    private static final long serialVersionUID = 1632881607904439683L;
    @ApiModelProperty(value = "旧密码")
    private String oldPass;
    @ApiModelProperty(value = "新密码")
    private String newPass;

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }
}

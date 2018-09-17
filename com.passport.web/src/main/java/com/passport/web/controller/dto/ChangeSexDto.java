package com.passport.web.controller.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/8/31 18:00
 */
public class ChangeSexDto implements Serializable {
    private static final long serialVersionUID = -1133807397912730601L;
    @ApiModelProperty(value = "新性别")
    private Integer sex;

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}

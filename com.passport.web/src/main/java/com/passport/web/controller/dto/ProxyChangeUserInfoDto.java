package com.passport.web.controller.dto;

import com.passport.service.constant.ChangeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/9/19 13:35
 */
@ApiModel(description = "代理修改用户资料")
public class ProxyChangeUserInfoDto implements Serializable {
    private static final long serialVersionUID = -7239942247640397984L;
    @ApiModelProperty(value = "账号")
    private String account;
    @ApiModelProperty(value = "修改类型")
    private ChangeType type;
    @ApiModelProperty(value = "值")
    private String value;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public ChangeType getType() {
        return type;
    }

    public void setType(ChangeType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

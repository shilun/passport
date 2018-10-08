package com.passport.main.controller.dto;

import com.passport.domain.module.ProxyServerChangeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/10/8 10:20
 */
@ApiModel(description = "添加服务器配置")
public class ProxyServerChangeDto implements Serializable {
    private static final long serialVersionUID = 8594914997872143544L;
    @ApiModelProperty(value = "修改类型")
    private ProxyServerChangeEnum changeEnum;
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "值")
    private String value;

    public ProxyServerChangeEnum getChangeEnum() {
        return changeEnum;
    }

    public void setChangeEnum(ProxyServerChangeEnum changeEnum) {
        this.changeEnum = changeEnum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

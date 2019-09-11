package com.passport.web.controller.dto;

import com.common.util.model.SexEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Luo
 * @date 2018/9/18 19:09
 */
@Data
@ApiModel(description = "代理添加用户")
public class ProxyAddUserDto implements Serializable {
    private static final long serialVersionUID = -6993863601384076871L;
    @ApiModelProperty(value = "账号")
    private String pin;
    @ApiModelProperty(value = "密码")
    private String pass;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "昵称")
    private String nickName;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "性别")
    private SexEnum sexType;
    @ApiModelProperty(value = "生日")
    private String birthDay;
    @ApiModelProperty(value = "头像地址")
    private String headUrl;
}

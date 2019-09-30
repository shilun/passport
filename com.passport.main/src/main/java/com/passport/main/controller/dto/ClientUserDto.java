package com.passport.main.controller.dto;

import com.common.util.AbstractDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.Date;


@Data
public class ClientUserDto extends AbstractDTO {
    /**
     * 用户pin
     */
    private String pin;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 电话
     */
    private String phone;
    /**
     * 邮件
     */
    private String email;
    /**
     * 性别
     */
    private Integer sexType;
    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 生日
     */
    private Date birthDay;

}

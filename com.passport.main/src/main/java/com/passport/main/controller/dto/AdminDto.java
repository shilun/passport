package com.passport.main.controller.dto;

import com.common.util.AbstractDTO;
import lombok.Data;

import java.io.Serializable;
@Data
public class AdminDto extends AbstractDTO implements Serializable {
    /**pin*/
    private String pin;
    /**妮称*/
    private String name;
    /**状态*/
    private Integer status;
    /**电话*/
    private String phone;
    /**邮件*/
    private String email;
    /**性别*/
    private Integer sexType;
    /**角色*/
    private String[] roles;
}

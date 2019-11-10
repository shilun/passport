package com.passport.main.controller.dto;

import lombok.Data;

@Data
public class IdChangePassDto {
    /**
     * 用户id
     */
    private String id;

    /**
     * 密码
     */
    private String password;
}

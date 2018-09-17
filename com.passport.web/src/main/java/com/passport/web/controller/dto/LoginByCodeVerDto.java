package com.passport.web.controller.dto;

/**
 * @author Luo
 * @date 2018/8/31 14:02
 */
public class LoginByCodeVerDto extends LoginByCodeDto {
    private static final long serialVersionUID = 4176323989252037271L;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

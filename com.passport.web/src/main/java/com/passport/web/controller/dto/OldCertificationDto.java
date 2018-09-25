package com.passport.web.controller.dto;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/9/25 14:48
 */
public class OldCertificationDto implements Serializable {
    private static final long serialVersionUID = 6536827255666832678L;
    private Integer userId;
    private String realName;
    private String idCard;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
}

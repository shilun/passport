package com.passport.rpc.dto;

import java.io.Serializable;

/**
 * @author Luo
 * @date 2018/9/3 19:50
 */
public class UserExtendDTO implements Serializable {
    private static final long serialVersionUID = -3942654533500567102L;
    // 用户编码
    private Integer userCode;
    //是否机器人
    private Integer isRobot;
    //签名
    private String sign;

    public Integer getUserCode() {
        return userCode;
    }

    public void setUserCode(Integer userCode) {
        this.userCode = userCode;
    }

    public Integer getIsRobot() {
        return isRobot;
    }

    public void setIsRobot(Integer isRobot) {
        this.isRobot = isRobot;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}

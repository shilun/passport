package com.passport.domain;

import com.common.util.AbstractBaseEntity;

/**
 * @author Luo
 * @date 2018/9/3 18:10
 */
public class ClientUserExtendInfo extends AbstractBaseEntity {
    // 用户编码
    private Integer userCode;
    //是否机器人  1.是  2.否
    private Integer robot;
    //签名
    private String sign;

    public Integer getUserCode() {
        return userCode;
    }

    public void setUserCode(Integer userCode) {
        this.userCode = userCode;
    }

    public Integer getRobot() {
        return robot;
    }

    public void setRobot(Integer robot) {
        this.robot = robot;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}

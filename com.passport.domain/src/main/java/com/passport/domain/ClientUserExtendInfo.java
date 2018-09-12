package com.passport.domain;

import com.common.util.AbstractBaseEntity;

/**
 * @author Luo
 * @date 2018/9/3 18:10
 */
public class ClientUserExtendInfo extends AbstractBaseEntity {
    // 用户编码
    private Integer userCode;
    // 头像URL
    private String headUrl;
    //是否机器人  1.是  0.否
    private int isRobot;
    //最后登陆IP
    private String lastLoginIp;
    //签名
    private String sign;
    //微信
    private String wechat;
    //身份证号
    private String idCard;
    //身份证上的名字
    private String realName;
    //qq
    private Long QQ;

    public int getUserCode() {
        return userCode;
    }

    public void setUserCode(int userCode) {
        this.userCode = userCode;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public int getIsRobot() {
        return isRobot;
    }

    public void setIsRobot(int isRobot) {
        this.isRobot = isRobot;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Long getQQ() {
        return QQ;
    }

    public void setQQ(Long QQ) {
        this.QQ = QQ;
    }
}

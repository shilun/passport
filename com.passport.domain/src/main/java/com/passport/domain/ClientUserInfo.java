package com.passport.domain;

import com.common.util.AbstractBaseEntity;
import com.common.util.IUserEntity;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

/**
 * 客户用户信息
 */
public class ClientUserInfo extends AbstractBaseEntity implements IUserEntity {
    /**
     * 用户pin
     */
    private String pin;

    /**
     * 第三方账户
     */
    private String refId;
    /**
     * 代理商id
     */
    private Long proxyId;
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
     * 密码
     */
    private String passwd;
    /**
     * 性别
     */
    private Integer sexType;
    /**
     * 用户状态
     */
    private Integer status;
    /**
     * 是否推广用户 1是  2否
     */
    private Integer popularize;


    private Date birthDay;

    /**
     * 注册ip
     */
    private String registerIp;

    // 头像URL
    private String headUrl;

    //微信
    private String wechat;
    //身份证号
    private String idCard;
    //身份证上的名字
    private String realName;
    //qq
    private Long qq;
    //(推荐)二维码图片名字
    private String qrName;

    //签名
    private String sign;

    //上线用户
    private String upPin;

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public Long getProxyId() {
        return proxyId;
    }

    public void setProxyId(Long proxyId) {
        this.proxyId = proxyId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public Integer getSexType() {
        return sexType;
    }

    public void setSexType(Integer sexType) {
        this.sexType = sexType;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
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

    public Long getQq() {
        return qq;
    }

    public void setQq(Long qq) {
        this.qq = qq;
    }

    public String getQrName() {
        return qrName;
    }


    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Deprecated
    public void setQrName(String qrName) {
        this.qrName = qrName;
    }

    public String getUpPin() {
        return upPin;
    }

    public void setUpPin(String upPin) {
        this.upPin = upPin;
    }

    public Integer getPopularize() {
        return popularize;
    }

    public void setPopularize(Integer popularize) {
        this.popularize = popularize;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this).toString();
    }
}

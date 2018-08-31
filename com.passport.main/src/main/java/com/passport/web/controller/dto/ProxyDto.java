package com.passport.web.controller.dto;

import com.common.util.AbstractDTO;

/**
 * 代理商信息
 */
public class ProxyDto  extends AbstractDTO {
    /**
     * 用户pin
     */
    private String pin;

    /**
     * 公司名称
     */
    private String name;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 联系人
     */
    private String linkMan;

    /**
     * 接入token
     */
    private String token;

    /**
     * 接入加密key uuid生成
     */
    private String encodingKey;
    /**
     * 网站
     */
    private String url;
    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private Integer status;
    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEncodingKey() {
        return encodingKey;
    }

    public void setEncodingKey(String encodingKey) {
        this.encodingKey = encodingKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

package com.passport.main.controller.dto;

import com.common.util.AbstractDTO;

/**
 * 电话内容
 */
public class SmsInfoDto extends AbstractDTO {
    /**
     * 代理商id
     */
    private String proxyId;
    /**
     * 电话号码
     */
    private String phone;

    /**
     * 电话内容
     */
    private String contnet;

    public String getProxyId() {
        return proxyId;
    }

    public void setProxyId(String proxyId) {
        this.proxyId = proxyId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContnet() {
        return contnet;
    }

    public void setContnet(String contnet) {
        this.contnet = contnet;
    }
}

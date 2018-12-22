package com.passport.main.controller.dto;

public class ProxyUserPasswordChangeDto extends PasswordChangeDto {

    private Long proxyId;

    private Long userId;

    public Long getProxyId() {
        return proxyId;
    }

    public void setProxyId(Long proxyId) {
        this.proxyId = proxyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

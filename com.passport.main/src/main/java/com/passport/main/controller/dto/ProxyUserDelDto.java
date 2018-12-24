package com.passport.main.controller.dto;

import java.io.Serializable;

public class ProxyUserDelDto implements Serializable {
    private Long proxyId;
    private Long id;

    public Long getProxyId() {
        return proxyId;
    }

    public void setProxyId(Long proxyId) {
        this.proxyId = proxyId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

package com.passport.domain;

import com.common.util.AbstractBaseEntity;

public class AppConfig extends AbstractBaseEntity {
    private Long proxyId;
    String content;

    public Long getProxyId() {
        return proxyId;
    }

    public void setProxyId(Long proxyId) {
        this.proxyId = proxyId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

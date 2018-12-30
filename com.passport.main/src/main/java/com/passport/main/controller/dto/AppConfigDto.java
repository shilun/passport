package com.passport.main.controller.dto;

import com.common.util.AbstractDTO;

public class AppConfigDto extends AbstractDTO {
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

package com.passport.main.controller.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProxyUserDelDto implements Serializable {
    private Long proxyId;
    private String id;
}

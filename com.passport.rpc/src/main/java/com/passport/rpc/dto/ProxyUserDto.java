package com.passport.rpc.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProxyUserDto implements Serializable {
    private Long seqId;
    private String loginToken;
    private String account;
    /**
     * 代理商
     */
    private Long proxyId;
    /**
     * 账户
     */
    private String pin;
    /**
     * 电话
     */
    private String phone;
    /**
     * 备注
     */
    private String desc;
    //1 运营 2 财务 3 配置
    private Long[] roles;

    /**
     * 密码
     */
    private String pass;

    /**
     * 用户状态
     */
    private Integer status;
}

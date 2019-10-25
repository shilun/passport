package com.passport.domain;

import com.common.util.AbstractBaseEntity;
import lombok.Data;

@Data
public class ProxyUserInfo extends AbstractBaseEntity {
    /**
     * 代理商
     */
    private String proxyId;
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
    /**
     * 资源
     */
    private String[] resources;

    /**
     * 密码
     */
    private String pass;

    /**
     * 用户状态
     */
    private Integer status;

}

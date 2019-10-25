package com.passport.rpc.dto;

import com.common.util.AbstractDTO;
import lombok.Data;

@Data
public class ProxyUserDto extends AbstractDTO {
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
     * 用户状态
     */
    private Integer status;

    private String token;
}

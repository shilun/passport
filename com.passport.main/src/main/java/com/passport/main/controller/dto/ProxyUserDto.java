package com.passport.main.controller.dto;

import com.common.util.AbstractDTO;
import lombok.Data;

@Data
public class ProxyUserDto  extends AbstractDTO {
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
    private String[] roles;


    /**
     * 用户状态
     */
    private Integer status;
}

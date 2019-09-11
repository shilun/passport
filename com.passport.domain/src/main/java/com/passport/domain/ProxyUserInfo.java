package com.passport.domain;

import com.common.util.AbstractBaseEntity;
import com.common.util.AbstractSeqEntity;
import lombok.Data;
import org.springframework.data.annotation.Transient;

/**
 * 代理商用户
 */
@Data
public class ProxyUserInfo extends AbstractSeqEntity {

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
     * 密码
     */
    private String pass;

    /**
     * 用户状态
     */
    private Integer status;
}

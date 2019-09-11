package com.passport.domain;

import com.common.util.AbstractSeqEntity;
import lombok.Data;

/**
 * 管理员用户信息
 */
@Data
public class AdminUserInfo extends AbstractSeqEntity {
    /**pin*/
    private String pin;
    /**妮称*/
    private String name;
    /**密码*/
    private String passwd;
    /**状态*/
    private Integer status;
    /**电话*/
    private String phone;
    /**邮件*/
    private String email;
    /**性别*/
    private Integer sexType;
    /**角色*/
    private String[] roles;
}

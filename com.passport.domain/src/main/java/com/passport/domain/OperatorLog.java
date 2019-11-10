package com.passport.domain;

import com.common.util.AbstractBaseEntity;
import lombok.Data;

/**
 * 操作日志
 */
@Data
public class OperatorLog extends AbstractBaseEntity {
    /**
     * 子系统
     */
    String system;
    /**
     * 用户
     */
    String pin;
    /**
     * 动作
     */
    String action;
    /**
     * 备注
     */
    String remark;

}

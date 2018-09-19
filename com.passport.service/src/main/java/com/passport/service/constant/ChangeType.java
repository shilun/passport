package com.passport.service.constant;

import com.common.util.IGlossary;

/**
 * @author Luo
 * @date 2018/9/19 13:55
 */
public enum ChangeType implements IGlossary {
    PASS("密码",1),
    BIRTH("生日",2),
    PHONE("手机号",3),
    EMAIL("邮箱",4);
    private String name;
    private Integer value;

    ChangeType(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}

package com.passport.rpc.dto;

import com.common.util.IGlossary;

/**
 * @author Luo
 * @date 2018/10/22 19:12
 */
public enum LimitType implements IGlossary {
    Login("登陆",1),
    Register("注册",2);

    private String name;
    private Integer value;

    LimitType(String name, Integer value) {
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

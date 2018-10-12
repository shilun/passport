package com.passport.rpc.dto;

import com.common.util.IGlossary;

/**
 * @author Luo
 * @date 2018/10/11 14:37
 */
public enum ChangeType implements IGlossary {
    NICK("昵称", 1);

    ChangeType(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    private String name;
    private Integer value;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}

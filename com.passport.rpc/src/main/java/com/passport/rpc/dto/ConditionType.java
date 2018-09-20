package com.passport.rpc.dto;

import com.common.util.IGlossary;

/**
 * 根据条件只能查到唯一结果
 * @author Luo
 * @date 2018/9/20 16:39
 */
public enum ConditionType implements IGlossary {

    ID("id", 1),
    ACCOUNT("账号", 2);

    ConditionType(String name, Integer value) {
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

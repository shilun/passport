package com.passport.rpc.dto;

import com.common.util.IGlossary;

/**
 * @author Luo
 * @date 2018/9/19 15:52
 */
public enum DateType implements IGlossary {
    ALL("全部", 1),
    DAY("今日", 2),
    WEEK("本周", 3),
    MONTH("本月", 4);

    DateType(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    private String name;
    private Integer value;


    @Override
    public String getName() {
        return null;
    }

    @Override
    public Integer getValue() {
        return null;
    }
}

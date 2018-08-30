package com.passport.domain.module;

import com.common.util.IGlossary;

/**
 * Created by shilun on 2016/8/15.
 */
public enum UserStatusEnum implements IGlossary {
    Normal("正常",1),
    Disable("停用",2),
    Temp("临时",3);
    private String name;
    private Integer value;

    UserStatusEnum(String name, Integer value) {
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

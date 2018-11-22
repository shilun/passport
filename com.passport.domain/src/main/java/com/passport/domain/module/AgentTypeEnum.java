package com.passport.domain.module;

import com.common.util.IGlossary;

/**
 * Created by shilun on 2017/5/10.
 */
public enum AgentTypeEnum implements IGlossary {
    Android(1, "Android"),
    IOS(2, "IOS"),
    Other(3, "Other");

    private Integer value;
    private String name;

    AgentTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }
}

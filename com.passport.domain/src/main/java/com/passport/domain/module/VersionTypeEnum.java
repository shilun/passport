package com.passport.domain.module;

import com.common.util.IGlossary;

/**
 */
public enum VersionTypeEnum implements IGlossary {
    Increment(1, "增量"),
    Full(2, "全量");

    private Integer value;
    private String name;

    VersionTypeEnum(Integer value, String name) {
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

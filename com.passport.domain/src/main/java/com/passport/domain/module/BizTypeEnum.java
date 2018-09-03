package com.passport.domain.module;

import com.common.util.IGlossary;

public enum BizTypeEnum implements IGlossary {

    QIPAI("棋牌", 1),
    CAIPIAO("彩票", 2),
    SMALLGAME("小游戏", 3);

    BizTypeEnum(String name, Integer value) {
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

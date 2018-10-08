package com.passport.domain.module;

import com.common.util.IGlossary;

/**
 * @author Luo
 * @date 2018/10/8 11:46
 */
public enum GameTypeEnum implements IGlossary {
    Qipai("棋牌",1),
    Caipiao("彩票",2),
    Farm("农场",3);
    private String name;
    private Integer value;

    GameTypeEnum(String name, Integer value) {
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

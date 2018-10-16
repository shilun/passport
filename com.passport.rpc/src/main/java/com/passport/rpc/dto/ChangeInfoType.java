package com.passport.rpc.dto;

import com.common.util.IGlossary;

/**
 * @author Luo
 * @date 2018/10/11 14:37
 */
public enum ChangeInfoType implements IGlossary {
    NICK("昵称", 1);

    ChangeInfoType(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    private String name;
    private Integer value;

    public static ChangeInfoType valueOf(Integer value){
        switch (value){
            case 1:return NICK;
            default:
        }
        return null;
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

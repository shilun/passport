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
    MONTH("本月", 4),
    YEAY("本年", 5);

    DateType(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    private String name;
    private Integer value;

    public static DateType valueOf(Integer value) throws Exception{
        switch (value){
            case 1:return ALL;
            case 2:return DAY;
            case 3:return WEEK;
            case 4:return MONTH;
            case 5:return YEAY;
            default: throw new Exception("DateType undefine. type=" + value);
        }
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

package com.passport.domain.module;

import com.common.util.IGlossary;

/**
 * Created by shilun on 2016/8/15.
 */
public enum MailInfoStatusEnum implements IGlossary {
    Wait("待发送",1),
    SendSuccess("发送成功",2),
    SendFaile("发送失败",3),
    Cancle("取消",4);
    private String name;
    private Integer value;

    MailInfoStatusEnum(String name, Integer value) {
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

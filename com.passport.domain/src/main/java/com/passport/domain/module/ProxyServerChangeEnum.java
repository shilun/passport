package com.passport.domain.module;

import com.common.util.IGlossary;

/**
 * @author Luo
 * @date 2018/10/8 10:17
 */
public enum ProxyServerChangeEnum implements IGlossary {
    Ip("ip",1),
    Port("端口",2),
    Evironment("运行环境",3),
    Close("关闭",4);

    private String name;
    private Integer value;

    ProxyServerChangeEnum(String name, Integer value) {
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

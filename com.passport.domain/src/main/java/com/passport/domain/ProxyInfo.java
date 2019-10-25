package com.passport.domain;

import com.common.annotation.QueryField;
import com.common.mongo.QueryType;
import com.common.util.AbstractBaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.Date;

@Data
public class ProxyInfo extends AbstractBaseEntity {
    /**
     * 公司名称
     */
    private String name;
    /**
     * 结整时间
     */
    private Date endTime;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 联系人
     */
    private String linkMan;
    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private Integer status;
    /**
     * 自定义注册ui
     */
    private Integer selfReg;

    /**
     * 域名
     */
    private String domain[];

    @Transient
    @QueryField(name = "domain", type = QueryType.IN)
    private String inDomain[];
}

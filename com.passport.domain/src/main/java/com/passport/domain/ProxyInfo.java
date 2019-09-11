package com.passport.domain;

import com.common.annotation.QueryField;
import com.common.mongo.QueryType;
import com.common.util.AbstractBaseEntity;
import com.common.util.AbstractSeqEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 代理商信息
 */
@Data
public class ProxyInfo extends AbstractSeqEntity {


    /**
     * 代理的游戏
     * 1 棋牌 2 彩票 3 小游戏
     */
    private Integer[] games;

    /**
     * 结整时间
     */
    private Date endTime;
    /**
     * 公司名称
     */
    private String name;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 联系人
     */
    private String linkMan;

    /**
     * 接入token
     */
    private String token;

    /**
     * 网站
     */
    private String[] domain;
    @Transient
    @QueryField(name = "domain", type = QueryType.IN)
    private String[] inDomain;
    /**
     * 备注
     */
    private String remark;

    /**
     * 状态
     */
    private Integer status;
    private String pass;
    private String headImgUrl;
    private String realName;
    /**
     * 彩票费率
     */
    private BigDecimal cpRate;
    /**
     * 棋牌费率
     */
    private BigDecimal qpRate;
    /**
     * 服务费
     */
    private BigDecimal serviceMoney;
}

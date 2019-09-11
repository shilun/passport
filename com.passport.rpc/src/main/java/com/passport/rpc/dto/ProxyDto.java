package com.passport.rpc.dto;

import com.common.util.AbstractDTO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 代理商信息
 */
@Data
public class ProxyDto extends AbstractDTO implements Serializable {

    private Long seqId;
    /**
     * 代理的游戏
     * 1 棋牌 2 彩票 3 小游戏
     */
    private Integer[] games;
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
     * 网站
     */
    private String[] domain;
    /**
     * 备注
     */
    private String remark;
    /**
     * 结整时间
     */
    private String endTime;

    /**
     * 接入token
     */
    private String token;

    private String loginToken;

    /**
     * 接入加密key uuid生成
     */
    private String encodingKey;
    /**
     * 状态
     */
    private Integer status;
    private String headImgUrl;
    private String realName;
    private transient String account;

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

package com.passport.domain;

import com.common.annotation.QueryField;
import com.common.mongo.QueryType;
import com.common.util.AbstractBaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 代理商信息
 */
public class ProxyInfo extends AbstractBaseEntity {


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
     * 接入加密key uuid生成
     */
    private String encodingKey;
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

    public BigDecimal getCpRate() {
        return cpRate;
    }

    public void setCpRate(BigDecimal cpRate) {
        this.cpRate = cpRate;
    }

    public BigDecimal getQpRate() {
        return qpRate;
    }

    public void setQpRate(BigDecimal qpRate) {
        this.qpRate = qpRate;
    }

    public BigDecimal getServiceMoney() {
        return serviceMoney;
    }

    public void setServiceMoney(BigDecimal serviceMoney) {
        this.serviceMoney = serviceMoney;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEncodingKey() {
        return encodingKey;
    }

    public void setEncodingKey(String encodingKey) {
        this.encodingKey = encodingKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String[] getDomain() {
        return domain;
    }

    public void setDomain(String[] domain) {
        this.domain = domain;
    }

    public String[] getInDomain() {
        return inDomain;
    }

    public void setInDomain(String[] inDomain) {
        this.inDomain = inDomain;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer[] getGames() {
        return games;
    }

    public void setGames(Integer[] games) {
        this.games = games;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}

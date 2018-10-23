package com.passport.service;

import com.common.mongo.MongoService;
import com.passport.domain.LimitInfo;
import com.passport.rpc.dto.LimitType;

import java.util.Date;

/**
 * @author Luo
 * @date 2018/10/22 19:40
 */
public interface LimitInfoService extends MongoService<LimitInfo> {
    /**
     * 根据pin查询限制信息
     * @param proxyId
     * @param pin
     * @return
     */
    LimitInfo findByPin(Long proxyId, String pin);
    /**
     * 根据ip查询限制信息
     * @param ip
     * @return
     */
    LimitInfo findByIp(String ip);

    /**
     * 添加信息(对pin只能限制登陆)
     * @param proxyId
     * @param pin
     * @param limitStartTime
     * @param limitEndTime
     * @return
     */
    void addLimitInfo(Long proxyId, String pin, Date limitStartTime, Date limitEndTime,String remarks);

    /**
     * 添加信息
     * @param ip
     * @param limitType
     * @param limitStartTime
     * @param limitEndTime
     * @return
     */
    void addLimitInfo(String ip, LimitType limitType,Date limitStartTime, Date limitEndTime,String remarks);

    /**
     * 查询单ip的注册限制数量
     * @return
     */
    LimitInfo findAllLimitNum();

    /**
     * 设置单ip注册数量
     * @param allNum
     * @return
     */
    void setLimitRegisterNum(Integer allNum);

    /**
     * 给当前ip的当前注册数量加一
     * @param ip
     */
    void addIpRegisterNum(String ip);


}

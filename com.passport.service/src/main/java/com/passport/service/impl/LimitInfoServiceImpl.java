package com.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.common.util.StringUtils;
import com.passport.domain.LimitInfo;
import com.passport.rpc.dto.LimitType;
import com.passport.service.LimitInfoService;
import com.passport.service.constant.SysContant;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Luo
 * @date 2018/10/22 19:53
 */
@Service
public class LimitInfoServiceImpl extends AbstractMongoService<LimitInfo> implements LimitInfoService {
    @Override
    protected Class getEntityClass() {
        return LimitInfo.class;
    }

    @Override
    public LimitInfo findByPin(Long proxyId, String pin) {
        LimitInfo limitInfo = new LimitInfo();
        limitInfo.setProxyId(proxyId);
        limitInfo.setPin(pin);
        return this.findByOne(limitInfo);
    }

    @Override
    public LimitInfo findByIp(String ip) {
        LimitInfo limitInfo = new LimitInfo();
        limitInfo.setIp(ip);
        return this.findByOne(limitInfo);
    }

    @Override
    public LimitInfo findAllLimitNum() {
        LimitInfo limitInfo = new LimitInfo();
        limitInfo.setName(SysContant.LIMIT_IP_REGISTER_NUM_NAME);
        return this.findByOne(limitInfo);
    }

    @Override
    public void setLimitRegisterNum(Integer allNum) {
        LimitInfo limitInfo = new LimitInfo();
        limitInfo.setName(SysContant.LIMIT_IP_REGISTER_NUM_NAME);
        //先查询是否存在
        LimitInfo res = this.findByOne(limitInfo);
        if(res == null){
            res = limitInfo;
        }
        res.setAllNum(allNum);
        save(res);
    }

    @Override
    public void addIpRegisterNum(String ip) {
        if(StringUtils.isBlank(ip)){
            return;
        }
        LimitInfo limitInfo = findByIp(ip);
        if(limitInfo == null){
            limitInfo = new LimitInfo();
            limitInfo.setIp(ip);
            limitInfo.setCurrentIpRegNum(1);
        }else{
            limitInfo.setIp(ip);
            Integer currentNum = limitInfo.getCurrentIpRegNum();
            limitInfo.setCurrentIpRegNum(currentNum + 1);
        }
        save(limitInfo);
    }
}

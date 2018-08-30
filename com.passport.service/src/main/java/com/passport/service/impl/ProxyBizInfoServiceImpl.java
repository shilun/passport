package com.passport.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.common.mongo.AbstractMongoService;
import com.passport.domain.ProxyBizInfo;
import com.passport.domain.ProxyInfo;
import com.passport.service.ProxyBizInfoService;
import com.passport.service.ProxyInfoService;

/**
 * 代理商业务信息
 */
@Service
public class ProxyBizInfoServiceImpl extends AbstractMongoService<ProxyBizInfo> implements ProxyBizInfoService {

    @Override
    protected Class getEntityClass() {
        return ProxyBizInfo.class;
    }
}

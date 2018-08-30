package com.passport.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.common.mongo.AbstractMongoService;
import com.passport.domain.ProxyInfo;
import com.passport.service.ProxyInfoService;

@Service
public class ProxyInfoServiceImpl extends AbstractMongoService<ProxyInfo> implements ProxyInfoService {

    @Override
    protected Class getEntityClass() {
        return ProxyInfo.class;
    }
}

package com.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.passport.domain.ProxyInfo;
import com.passport.service.ProxyInfoService;
import org.springframework.stereotype.Service;

@Service
public class ProxyInfoServiceImpl extends AbstractMongoService<ProxyInfo> implements ProxyInfoService {

    @Override
    protected Class getEntityClass() {
        return ProxyInfo.class;
    }
}

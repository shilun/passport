package com.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.passport.domain.ProxyInfo;
import com.passport.domain.ProxyUserInfo;
import com.passport.service.ProxyInfoService;
import com.passport.service.ProxyUserInfoService;
import org.springframework.stereotype.Service;

@Service
public class ProxyUserInfoServiceImpl extends AbstractMongoService<ProxyUserInfo> implements ProxyUserInfoService {

    @Override
    protected Class getEntityClass() {
        return ProxyUserInfo.class;
    }
}

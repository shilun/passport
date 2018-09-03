package com.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.passport.domain.ProxyBizInfo;
import com.passport.service.ProxyBizInfoService;
import org.springframework.stereotype.Service;

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

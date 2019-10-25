package com.passport.service.impl;

import com.common.exception.BizException;
import com.common.mongo.AbstractMongoService;
import com.passport.domain.ProxyInfo;
import com.passport.domain.RoleInfo;
import com.passport.service.ProxyInfoService;
import com.passport.service.RoleInfoService;
import org.springframework.stereotype.Service;

@Service
public class ProxyInfoServiceImpl extends AbstractMongoService<ProxyInfo> implements ProxyInfoService {

    @Override
    protected Class getEntityClass() {
        return ProxyInfo.class;
    }

    @Override
    public ProxyInfo findByDomain(String domain) {
        ProxyInfo query = new ProxyInfo();
        query.setInDomain(new String[]{domain});
        ProxyInfo info = findByOne(query);
        if (info == null) {
            throw new BizException("domain.error", "域名不存在");
        }
        return info;
    }
}

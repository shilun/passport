package com.passport.service;

import com.common.mongo.MongoService;
import com.passport.domain.ProxyInfo;

public interface ProxyInfoService extends MongoService<ProxyInfo> {
    /**
     * 根据域名获取代理商
     * @param domain
     * @return
     */
    ProxyInfo findByDomain(String domain);
}

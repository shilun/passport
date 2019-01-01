package com.passport.service;

import com.common.mongo.MongoService;
import com.passport.domain.AppConfig;

public interface AppConfigService extends MongoService<AppConfig> {
    String findByProxyId(Long proxyId);
}

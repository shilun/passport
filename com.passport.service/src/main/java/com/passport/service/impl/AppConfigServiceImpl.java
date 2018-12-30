package com.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.passport.domain.AppConfig;
import com.passport.service.AppConfigService;
import org.springframework.stereotype.Service;

@Service
public class AppConfigServiceImpl  extends AbstractMongoService<AppConfig> implements AppConfigService {
    @Override
    protected Class getEntityClass() {
        return AppConfig.class;
    }
}

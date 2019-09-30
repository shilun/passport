package com.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.passport.domain.AppConfig;
import com.passport.service.AppConfigService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@Service
public class AppConfigServiceImpl extends AbstractMongoService<AppConfig> implements AppConfigService {
    @Override
    protected Class getEntityClass() {
        return AppConfig.class;
    }

    private static String APP_CONFIG_KEY = "passport.app.config.{0}";

    @Resource
    private RedisTemplate redisTemplate;
}

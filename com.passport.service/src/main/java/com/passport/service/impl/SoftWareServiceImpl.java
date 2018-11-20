package com.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.passport.domain.SoftWare;
import com.passport.domain.module.AgentTypeEnum;
import com.passport.service.SoftWareService;
import org.springframework.stereotype.Service;

@Service
public class SoftWareServiceImpl extends AbstractMongoService<SoftWare> implements SoftWareService {
    @Override
    protected Class getEntityClass() {
        return SoftWare.class;
    }

    @Override
    public SoftWare findLastInfo(Long agentId, AgentTypeEnum type) {
        return null;
    }

}

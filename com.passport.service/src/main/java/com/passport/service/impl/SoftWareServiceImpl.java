package com.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.common.util.model.YesOrNoEnum;
import com.passport.domain.SoftWare;
import com.passport.domain.module.AgentTypeEnum;
import com.passport.domain.module.VersionTypeEnum;
import com.passport.service.SoftWareService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class SoftWareServiceImpl extends AbstractMongoService<SoftWare> implements SoftWareService {
    @Override
    protected Class getEntityClass() {
        return SoftWare.class;
    }

    @Override
    public SoftWare findLastInfo(Long proxyId, AgentTypeEnum type) {
        SoftWare query = new SoftWare();
        query.setProxyId(proxyId);
        query.setOsType(type.getValue());
        query.setStatus(YesOrNoEnum.YES.getValue());
        query.setVersionType(VersionTypeEnum.Full.getValue());
        Page<SoftWare> softWares = queryByPage(query, new PageRequest(0, 1));
        return softWares.getContent().get(0);
    }

}

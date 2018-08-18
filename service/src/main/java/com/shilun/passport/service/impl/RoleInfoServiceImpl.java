package com.shilun.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.shilun.passport.domain.RoleInfo;
import com.shilun.passport.service.RoleInfoService;
import org.springframework.stereotype.Service;

@Service
public class RoleInfoServiceImpl extends AbstractMongoService<RoleInfo> implements RoleInfoService {

    @Override
    protected Class getEntityClass() {
        return RoleInfo.class;
    }
}

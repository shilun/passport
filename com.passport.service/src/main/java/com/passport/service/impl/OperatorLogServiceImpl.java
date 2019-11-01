package com.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.passport.domain.OperatorLog;
import com.passport.service.OperatorLogService;
import org.springframework.stereotype.Service;

@Service
public class OperatorLogServiceImpl extends AbstractMongoService<OperatorLog> implements OperatorLogService {
    @Override
    protected Class getEntityClass() {
        return OperatorLog.class;
    }


    public void logInfo(String system, String pin, String action, String remark){
        OperatorLog entity = new OperatorLog();
        entity.setAction(action);
        entity.setRemark(remark);
        entity.setSystem(system);
        entity.setPin(pin);
        insert(entity);
    }
}

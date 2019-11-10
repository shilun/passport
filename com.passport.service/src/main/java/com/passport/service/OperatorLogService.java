package com.passport.service;

import com.common.mongo.MongoService;
import com.passport.domain.OperatorLog;

public interface OperatorLogService extends MongoService<OperatorLog> {
    /**
     * 记录操作日志
     * @param system
     * @param pin
     * @param action
     * @param remark
     */
    void logInfo(String system, String pin, String action, String remark);
}

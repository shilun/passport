package com.passport.rpc;

import com.common.util.RPCResult;

/**
 * 操作日志记录
 */
public interface OperatorRPCService {
    /**
     * 用户操作日志
     * @param system
     * @param pin
     * @param action
     * @param remark
     * @return
     */
    RPCResult<Boolean> logInfo(String system, String pin, String action, String remark);
}

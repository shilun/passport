package com.passport.service.rpc;

import com.common.util.RPCResult;
import com.passport.rpc.OperatorRPCService;
import com.passport.service.OperatorLogService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

@org.apache.dubbo.config.annotation.Service
@org.springframework.stereotype.Service
@Slf4j
public class OperatorRPCServiceImpl implements OperatorRPCService {

    @Resource
    private OperatorLogService operatorLogService;

    @Override
    public RPCResult<Boolean> logInfo(String system, String pin, String action, String remark) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            operatorLogService.logInfo(system, pin, action, remark);
            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            log.error("OperatorRPCService.logInfo.error", e);
            result.setCode("logInfo.error");
            result.setMessage("记录日志报错");
        }
        return result;
    }
}

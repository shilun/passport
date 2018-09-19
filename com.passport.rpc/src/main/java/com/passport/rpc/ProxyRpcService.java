package com.passport.rpc;

import com.common.util.RPCResult;
import com.passport.rpc.dto.DateType;
import com.passport.rpc.dto.LogLoginDto;
import com.passport.rpc.dto.ProxyDto;

import java.util.Date;

/**
 * 查询代理商信息
 */
public interface ProxyRpcService {
    /**
     * 根据域名查询代理商信息
     * @param domain
     * @return
     */
    RPCResult<ProxyDto> findByDomain(String domain);

    RPCResult<ProxyDto> login(String account,String pass);

    RPCResult<Boolean> changePass(String account,String oldPass,String newPass);

    /**
     * 查询活跃人数
     * @param startTime
     * @param endTime
     * @return
     */
    RPCResult<Long> QueryActiveUsers(Date startTime, Date endTime);

    /**
     * 查询新增用户
     * @param startTime
     * @param endTime
     * @return
     */
    RPCResult<Long> QueryNewUsers(Date startTime, Date endTime);

    RPCResult<Long> QueryActiveUsers(DateType type);

    RPCResult<Long> QueryNewUsers(DateType type);
}

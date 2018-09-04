package com.passport.rpc;

import com.common.util.RPCResult;
import com.passport.rpc.dto.ProxyDto;

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
}

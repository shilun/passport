package com.passport.rpc;

import com.common.rpc.StatusRpcService;
import com.common.util.RPCResult;
import com.passport.rpc.dto.*;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

/**
 * 查询代理商信息
 */
public interface ProxyRpcService extends StatusRpcService {
    /**
     * 查询所有代理商
     *
     * @return
     */
    public RPCResult<List<ProxyDto>> query(ProxyDto dto);

    /**
     * 查询所有代理商
     *
     * @return
     */
    public RPCResult<List<ProxyDto>> queryAll();

    /**
     * 根据id 查找代理商信息
     *
     * @param proxyId
     * @return
     */
    public RPCResult<ProxyDto> findById(Long proxyId);

    /**
     * 根据域名查询代理商信息
     *
     * @param domain
     * @return
     */
    RPCResult<ProxyDto> findByDomain(String domain);


    /**
     * 根据pin查询用户信息
     *
     * @param proxyId
     * @param pin
     * @return
     */
    @Deprecated
    RPCResult<ProxyDto> findByPin(Long proxyId, String pin);


    RPCResult<Boolean> changeInfo(ProxyDto proxyDto);

}

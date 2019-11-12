package com.passport.rpc;

import com.common.util.RPCResult;
import com.passport.rpc.dto.ProxyDto;
import com.passport.rpc.dto.ProxyUserDto;

import java.util.List;

public interface ProxyInfoRPCService {
    /**
     * 登录
     *
     * @param account
     * @param pass
     * @return
     */
    RPCResult<ProxyUserDto> login(String domain, String account, String pass);

    /**
     * 用户登出
     *
     * @param token
     * @return
     */
    RPCResult logOut(String token);

    /**
     * 验证用户token
     *
     * @param token
     * @return
     */
    RPCResult<ProxyUserDto> verificationToken(String token);

    /**
     * 添加用户
     *
     * @param proxyId
     * @param pin
     * @param phone
     * @param pass
     * @param desc
     * @param resources
     * @return
     */
    RPCResult<Boolean> addUser(String proxyId, String pin, String phone, String pass, String desc, String resources[]);


    /**
     * 管理员修改登录密码
     *
     * @param domain
     * @param pin
     * @param newPass
     * @return
     */
    RPCResult<Boolean> initPass(String domain, String pin, String newPass);

    /**
     * 修改密码
     *
     * @param token
     * @param oldPass
     * @param newPass
     * @return
     */
    RPCResult<Boolean> changePass(String token, String oldPass, String newPass);

    /**
     * 修改用户
     *
     * @param proxyId
     * @param pin
     * @param phone
     * @param desc
     * @param resources
     * @param status
     * @return
     */
    RPCResult<Boolean> upUser(String proxyId, String pin, String phone, String desc, String[] resources, Integer status);

    /**
     * 根据域名找查代理
     *
     * @param domain
     * @return
     */
    RPCResult<ProxyDto> findByDomain(String domain);

    /**
     * 根据id 查找代理
     *
     * @param proxyId
     * @return
     */
    RPCResult<ProxyDto> findById(String proxyId);

    /**
     * 查找所有代理商
     *
     * @return
     */
    RPCResult<List<ProxyDto>> queryAll();

}

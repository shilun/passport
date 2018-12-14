package com.passport.rpc;

import com.common.util.RPCResult;
import com.passport.rpc.dto.ProxyUserDto;

public interface ProxyUserRpcService {


    /**
     * 登录
     *
     * @param account
     * @param pass
     * @return
     */
    RPCResult<ProxyUserDto> login(Long proxyId, String account, String pass);

    /**
     * 添加用户
     * @param proxyId
     * @param phone
     * @param pass
     * @param desc
     * @return
     */
    RPCResult addUser(Long proxyId, String phone, String pass,String desc);

    /**
     * 登出
     *
     * @param loginToken
     * @return
     */
    RPCResult logOut(String loginToken);

    /**
     * 验证token
     *
     * @param loginToken
     * @return
     */
    RPCResult<ProxyUserDto> verfiyToken(String loginToken);

    /**
     * 登录密码
     *
     * @param account
     * @param oldPass
     * @param newPass
     * @return
     */
    RPCResult<Boolean> changePass(Long proxyId, String account, String oldPass, String newPass);

    /**
     * 修改用户角色
     *
     * @param proxyId
     * @param id
     * @param roles
     * @return
     */
    RPCResult<Boolean> changeRole(Long proxyId, Long id, Long roles[]);

    /**
     * @param proxyId
     * @param id
     * @param phone
     * @param desc
     * @param status
     * @return
     */
    RPCResult<Boolean> upUser(Long proxyId, Long id, String phone, String desc, Integer status);
}

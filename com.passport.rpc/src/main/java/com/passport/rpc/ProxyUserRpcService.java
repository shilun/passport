package com.passport.rpc;

import com.common.rpc.StatusRpcService;
import com.common.util.RPCResult;
import com.passport.rpc.dto.ProxyUserDto;

import java.util.List;

public interface ProxyUserRpcService extends StatusRpcService {


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
     * @param roles
     * @return
     */
    RPCResult addUser(Long proxyId, String phone, String pass,String desc,Long roles[]);

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
     * @param id
     * @param oldPass
     * @param newPass
     * @return
     */
    RPCResult<Boolean> changePass(Long cId,Long proxyId, Long id, String oldPass, String newPass);

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
     * 修改用户信息
     * @param proxyId
     * @param id
     * @param phone
     * @param desc
     * @param status
     * @param roles
     * @return
     */
    RPCResult<Boolean> upUser(Long proxyId, Long id, String phone, String desc, Integer status,Long roles[]);

    /**
     * 查看单个用户
     * @param proxyId
     * @param id
     * @return
     */
    RPCResult<ProxyUserDto> find(Long proxyId,Long id);

    /**
     * 查找代理商下所有用户
     * @param proxyId
     * @return
     */
    RPCResult<List<ProxyUserDto>> queryByProxyId(Long proxyId);
}

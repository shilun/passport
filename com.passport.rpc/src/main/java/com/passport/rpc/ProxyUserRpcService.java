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
     *
     * @param proxyId
     * @param phone
     * @param pass
     * @param desc
     * @param roles
     * @return
     */
    RPCResult addUser(Long proxyId, String phone, String pass, String desc, String roles[]);

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
     * 用户自已修改密码
     *
     * @param pin
     * @param oldPass
     * @param newPass
     * @return
     */
    RPCResult<Boolean> changePass(String pin, String oldPass, String newPass);


    /**
     * 管理员修改密码
     *
     * @param pin
     * @param newPass
     * @return
     */
    RPCResult<Boolean> changePass(String pin, String newPass);

    /**
     * 修改用户角色
     *
     * @param proxyId
     * @param pin
     * @param roles
     * @return
     */
    RPCResult<Boolean> changeRole(Long proxyId, Long pin, String roles[]);

    /**
     * @param pin
     * @param phone
     * @param desc
     * @param status
     * @param roles
     * @return
     */
    RPCResult<Boolean> upUser(String pin, String phone, String desc, Integer status, String roles[]);

    /**
     * 查看单个用户
     *
     * @param pin
     * @return
     */
    RPCResult<ProxyUserDto> find(String pin);

    /**
     * 查找代理商下所有用户
     *
     * @param proxyId
     * @return
     */
    RPCResult<List<ProxyUserDto>> queryByProxyId(Long proxyId);
}

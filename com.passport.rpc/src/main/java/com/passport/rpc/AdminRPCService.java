package com.passport.rpc;


import com.common.rpc.StatusRpcService;
import com.common.util.RPCResult;
import com.passport.rpc.dto.UserDTO;

import java.util.List;

/**
 * 管理员接口
 */
public interface AdminRPCService extends StatusRpcService {
    /**
     * 管理员登录1
     *
     * @param loginName 登录名
     * @param password  密码
     * @return
     */
    RPCResult<UserDTO> login(String loginName, String password);

    /**
     * 修改密码
     * @param token
     * @param oldPass
     * @param newPass
     * @return
     */
    RPCResult<Boolean> changePass(String token,String oldPass, String newPass);

    /**
     * 获取管理员角色
     *
     * @param pin
     * @return
     */
    RPCResult<List<String>> queryAdminRoles(String pin);

    /**
     * 验证用户token
     * @param token
     * @return
     */
    RPCResult<UserDTO> verfiyToken(String token);

    /**
     * 登出
     * @param token
     * @return
     */
    RPCResult<UserDTO> loginOut(String token);
}

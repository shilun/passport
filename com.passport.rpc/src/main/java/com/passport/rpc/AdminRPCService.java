package com.passport.rpc;


import com.common.util.RPCResult;
import com.passport.rpc.dto.UserDTO;

import java.util.List;

/**
 * 管理员接口
 */
public interface AdminRPCService {
    /**
     * 管理员登录1
     *
     * @param loginName 登录名
     * @param password  密码
     * @return
     */
    RPCResult<UserDTO> login(String loginName, String password);

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
    RPCResult<UserDTO> verificationToken(String token);

    /**
     * 登出
     * @param token
     * @return
     */
    RPCResult<UserDTO> loginOut(String token);
}

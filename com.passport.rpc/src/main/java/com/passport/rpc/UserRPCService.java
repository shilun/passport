package com.passport.rpc;

import com.common.rpc.StatusRpcService;
import com.common.util.RPCResult;
import com.passport.rpc.dto.*;

import java.util.Date;
import java.util.List;

/**
 * 客户webservice接口
 * Created by shilun on 16-12-5.
 */
public interface UserRPCService extends StatusRpcService {

    /**
     * 用户登录
     *
     * @param phone
     * @param pass
     * @return
     */
    RPCResult<UserDTO> login(String phone, String pass);

    /**
     * 根据用户pin查找用户
     *
     * @param pin
     * @return
     */
    RPCResult<UserDTO> findByPin(String pin);

    /**
     * 强制修改密码
     *
     * @param pin
     * @param pass
     * @return
     */

    RPCResult<Boolean> changePass(String pin, String pass);


    /**
     * 验证用户token
     * @param token
     * @return
     */
    RPCResult<UserDTO> verdifyToken(String token);

    /**
     * @param pin
     * @param oldPass
     * @param newPass
     * @return
     */

    RPCResult<Boolean> changePass(String pin, String oldPass, String newPass);
}

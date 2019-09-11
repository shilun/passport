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
     * @param proxyId
     * @param deviceId
     * @param agentType
     * @return
     */
    RPCResult<UserDTO> registerByDeviceId(Long proxyId, String deviceId, String agentType);

    /**
     * 根据用户pin查找用户
     *
     * @param proxyId
     * @param pin
     * @return
     */
    RPCResult<UserDTO> findByPin(Long proxyId, String pin);

    /**
     * 根据电话查找用户
     *
     * @param proxyId
     * @param mobile
     * @return
     */
    RPCResult<UserDTO> findByMobile(Long proxyId, String mobile);

    /**
     * 验证用户token
     *
     * @param token
     * @return
     */
    RPCResult<UserDTO> verfiyToken(String token);

    RPCResult<List<UserDTO>> query(UserDTO dto);


    /**
     * 修改基本信息
     *
     * @param dto
     * @return
     */
    RPCResult<Boolean> changeInfo(UserDTO dto);

    /**
     * 根据条件查询人数
     *
     * @param dto
     * @return
     */
    RPCResult<Long> queryUsersCount(UserDTO dto);


    RPCResult<Boolean> resetPass(String pin, String pass);
}

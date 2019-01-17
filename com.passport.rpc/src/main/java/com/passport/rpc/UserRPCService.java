package com.passport.rpc;

import com.common.util.RPCResult;
import com.passport.rpc.dto.*;

import java.util.Date;
import java.util.List;

/**
 * 客户webservice接口
 * Created by shilun on 16-12-5.
 */
public interface UserRPCService {

    /**
     * 查询用户code
     * @param proxyId
     * @param pin
     * @return
     */
    RPCResult<Long> findUserCodeByPin(Long proxyId,String pin);

    /**
     * 根据用户pin查找用户
     * @param proxyId
     * @param pin
     * @return
     */
    RPCResult<UserDTO> findByPin(Long proxyId,String pin);

    /**
     * 根据电话查找用户
     * @param proxyId
     * @param mobile
     * @return
     */
    RPCResult<UserDTO> findByMobile(Long proxyId,String mobile);

    /**
     * 验证用户token
     * @param token
     * @return
     */
    RPCResult<UserDTO> verfiyToken(String token);

    RPCResult<List<UserDTO>> query(UserDTO dto);

    /**
     * 获取用户最近一次的登陆信息
     * @param proxyId
     * @param pin
     * @return
     */
    RPCResult<LogLoginDto> getUserLastLoginInfo(Long proxyId,String pin);


    /**
     * 修改基本信息
     * @param dto
     * @return
     */
    RPCResult<Boolean> changeInfo(UserDTO dto);

    /**
     * 根据条件查询人数
     * @param dto
     * @return
     */
    RPCResult<Long> queryUsersCount(UserDTO dto);

    RPCResult<List<LogLoginDto>> queryLoginLog(LogLoginDto dto);

    RPCResult<UserDTO> queryUser(Long proxyId,Long userCode);


    /**
     * 添加推广 用户
     * @param proxyId
     * @param nickName
     * @param pass
     * @return
     */
    RPCResult<Boolean> addPopUser(Long proxyId,String nickName,String pass);


    RPCResult<Boolean> resetPass(Long proxyId,Long id,String pass);
}

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

    RPCResult<UserExtendDTO> findByUserCode(Long proxyId,Integer userCode);

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
     * 棋牌服务器校验token
     * @param token
     * @return
     */
    RPCResult<QipaiUserDTO> qipaiVerfiyToken(String token);

    /**
     * 获取用户最近一次的登陆信息
     * @param proxyId
     * @param pin
     * @return
     */
    RPCResult<LogLoginDto> getUserLastLoginInfo(Long proxyId,String pin);


    /**
     * 修改基本信息
     * @param proxyId
     * @param pin
     * @param type
     * @param value
     * @return
     */
    RPCResult<Boolean> changeInfo(Long proxyId, String pin, ChangeInfoType type, String value);

    /**
     * 获取限制信息
     * @param ip
     * @return
     */
    RPCResult<LimitDto> getLimitInfo(String ip);

    /**
     * 获取限制信息
     * @param proxyId
     * @param pin
     * @return
     */
    RPCResult<LimitDto> getLimitInfo(Long proxyId, String pin);

    /**
     * 对用户登陆或注册时的IP进行限制
     * @param ip
     * @param type
     * @param limitStartTime   开始限制时间
     * @param limitEndTime      结束限制时间
     * @param remarks   备注
     * @return
     */
    RPCResult<Boolean> userlimitIp(String ip, LimitType type, Date limitStartTime, Date limitEndTime,String remarks);

    /**
     * 对用户的pin进行限制登陆
     * @param proxyId
     * @param pin
     * @param limitStartTime
     * @param limitEndTime
     * @param remarks
     * @return
     */
    RPCResult<Boolean> userlimitPin(Long proxyId, String pin,Date limitStartTime, Date limitEndTime,String remarks);


    /**
     * 限制单ip注册数量
     * @param num
     * @return
     */
    RPCResult<Boolean> setLimitRegisterNum(Integer num);

    /**
     * 查询单ip注册的最大数量
     * @return
     */
    RPCResult<Integer> findAllLimitNum();

    /**
     * 查询在min与max之间的信息
     * @param min  注册的数量
     * @param max   注册的数量
     * @return
     */
    RPCResult<List<LimitDto>> getLimitInfo(Integer min,Integer max,LimitDto dto);

    /**
     * 根据限制类型来查找信息
     * @return
     */
    RPCResult<List<LimitDto>> getLimitInfo(LimitDto dto);

    /**
     * 根据条件查询人数
     * @param dto
     * @return
     */
    RPCResult<Long> queryUsersCount(UserDTO dto);
}

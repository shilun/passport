package com.passport.rpc;

import com.common.util.RPCResult;
import com.passport.rpc.dto.*;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

/**
 * 查询代理商信息
 */
public interface ProxyRpcService {
    /**
     * 查询所有代理商
     * @return
     */
    public RPCResult<List<ProxyDto>> query(ProxyDto dto);

    /**
     * 查询所有代理商
     * @return
     */
    public RPCResult<List<ProxyDto>> queryAll();
    /**
     * 根据id 查找代理商信息
     * @param proxyId
     * @return
     */
    public RPCResult<ProxyDto> findById(Long proxyId);

    /**
     * 重新生成token
     * @param proxyId
     * @return
     */
    public RPCResult<String> refreshToken(Long proxyId);

    /**
     * 修改加密串
     * @param proxyId
     * @param encodingKey
     * @return
     */
    public RPCResult upEncodingKey(Long proxyId,String encodingKey);
    /**
     * 根据域名查询代理商信息
     * @param domain
     * @return
     */
    RPCResult<ProxyDto> findByDomain(String domain);

    /**
     * 登录
     * @param account
     * @param pass
     * @return
     */
    RPCResult<ProxyDto> login(String account,String pass);

    /**
     * 登出
     * @param loginToken
     * @return
     */
    RPCResult logOut(String loginToken);

    /**
     * 验证token
     * @param loginToken
     * @return
     */
    RPCResult<ProxyDto> verfiyToken(String loginToken);

    /**
     * 登录密码
     * @param account
     * @param oldPass
     * @param newPass
     * @return
     */
    RPCResult<Boolean> changePass(String account,String oldPass,String newPass);

    /**
     * 查询活跃人数
     * @param startTime
     * @param endTime
     * @return
     */
    RPCResult<Long> queryActiveUsers(Long proxyId,Date startTime, Date endTime);

    /**
     * 查询新增用户
     * @param startTime
     * @param endTime
     * @return
     */
    RPCResult<Long> queryNewUsers(Long proxyId,Date startTime, Date endTime);

    /**
     * 查询活跃人数
     * @param proxyId
     * @param type
     * @return
     */
    RPCResult<Long> queryActiveUsers(Long proxyId,DateType type);

    /**
     * 查询新增人数
     * @param proxyId
     * @param type
     * @return
     */
    RPCResult<Long> queryNewUsers(Long proxyId,DateType type);

    /**
     * 查询留存率
     * @param startTime
     * @param endTime
     * @return
     */
    RPCResult<Double> queryRetention(Long proxyId,Date startTime, Date endTime);

    /**
     * 查询留存率
     * @param proxyId
     * @param type
     * @return
     */
    RPCResult<Double> queryRetention(Long proxyId,DateType type);

    /**
     * 根据注册时间段筛选用户
     * @param startTime
     * @param endTime
     * @return
     */
    RPCResult<List<UserDTO>> queryUsersByRegTime(Date startTime, Date endTime,UserDTO dto);

    /**
     * 根据条件查询用户详细信息
     * @param proxyId
     * @param type
     * @param data
     * @return
     */
    RPCResult<UserDTO> queryUsersByCondition(Long proxyId, ConditionType type, String data);
    /**
     * 根据昵称查询用户列表
     * @return
     */
    RPCResult<List<UserDTO>> queryUsersByNick(UserDTO dto);

    /**
     * 查询用户的上级信息
     * @param proxyId
     * @param phone
     * @return
     */
    RPCResult<ProxyDto> queryUsersSuperior(Long proxyId, String phone);

    /**
     * 根据pin查询用户信息
     * @param proxyId
     * @param pin
     * @return
     */
    RPCResult<ProxyDto> findByPin(Long proxyId, String pin);

    /**
     * 对用户登陆或注册时的IP进行限制
     * @param dto
     * @return
     */
    RPCResult<Boolean> userlimit(LimitDto dto);

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
    RPCResult<List<LimitDto>> getLimitInfo(Integer min, Integer max, LimitDto dto);

    /**
     * 根据限制类型来查找信息
     * @return
     */
    RPCResult<List<LimitDto>> getLimitInfo(LimitDto dto);

    RPCResult<Long> queryRegNum(Long proxyId,DateType type);

    RPCResult<Long> queryRegNum(Long proxyId,Date startDate,Date endDate);

    RPCResult<LimitDto> findLimitInfoById(Long id);

    RPCResult<Boolean> changeInfo(ProxyDto proxyDto);

    /**
     * 删除限制信息
     * @param dto
     * @return
     */
    RPCResult<Boolean> delLimitInfo(LimitDto dto);
}

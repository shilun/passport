package com.passport.rpc;

import com.common.util.RPCResult;
import com.passport.rpc.dto.DateType;
import com.passport.rpc.dto.LogLoginDto;
import com.passport.rpc.dto.ProxyDto;
import com.passport.rpc.dto.UserDTO;
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
    public RPCResult refreshToken(Long proxyId);

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
    RPCResult<Long> QueryActiveUsers(Long proxyId,Date startTime, Date endTime);

    /**
     * 查询新增用户
     * @param startTime
     * @param endTime
     * @return
     */
    RPCResult<Long> QueryNewUsers(Long proxyId,Date startTime, Date endTime);

    /**
     * 查询活跃人数
     * @param proxyId
     * @param type
     * @return
     */
    RPCResult<Long> QueryActiveUsers(Long proxyId,DateType type);

    /**
     * 查询新增人数
     * @param proxyId
     * @param type
     * @return
     */
    RPCResult<Long> QueryNewUsers(Long proxyId,DateType type);

    /**
     * 查询留存率
     * @param startTime
     * @param endTime
     * @return
     */
    RPCResult<Double> QueryRetention(Long proxyId,Date startTime, Date endTime);

    /**
     * 查询留存率
     * @param proxyId
     * @param type
     * @return
     */
    RPCResult<Double> QueryRetention(Long proxyId,DateType type);

    /**
     * 根据时间段筛选用户
     * @param proxyId
     * @param startTime
     * @param endTime
     * @return
     */
    RPCResult<Page<UserDTO>> QueryUsersByRegTime(Long proxyId, Date startTime, Date endTime,UserDTO dto);

    /**
     * 根据IP段筛选用户
     * @param proxyId
     * @param ip
     * @param dto
     * @return
     */
    RPCResult<Page<LogLoginDto>> QueryUsersByLoginIp(Long proxyId, String ip, UserDTO dto);
}

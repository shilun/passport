package com.passport.rpc;

import com.common.util.RPCResult;
import com.passport.rpc.dto.LogLoginDto;
import com.passport.rpc.dto.QipaiUserDTO;
import com.passport.rpc.dto.UserDTO;
import com.passport.rpc.dto.UserExtendDTO;
import org.springframework.data.domain.Page;

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

}

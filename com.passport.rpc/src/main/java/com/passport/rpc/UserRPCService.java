package com.passport.rpc;

import com.common.util.RPCResult;
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

    RPCResult<UserDTO> findByPin(Long proxyId,String pin);

    RPCResult<UserDTO> verfiyToken(String pin,String token);

    RPCResult<Page<UserDTO>> query(UserDTO dto);

}

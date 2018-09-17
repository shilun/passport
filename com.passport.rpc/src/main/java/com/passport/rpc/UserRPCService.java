package com.passport.rpc;

import com.common.util.RPCResult;
import com.passport.rpc.dto.UserDTO;
import com.passport.rpc.dto.UserExtendDTO;

/**
 * 客户webservice接口
 * Created by shilun on 16-12-5.
 */
public interface UserRPCService {

    UserExtendDTO findByUserCode(Long proxyId,Integer userCode);

    UserDTO findByPin(Long proxyId,String pin);

    UserDTO verToken(Long proxyId,String token);

}

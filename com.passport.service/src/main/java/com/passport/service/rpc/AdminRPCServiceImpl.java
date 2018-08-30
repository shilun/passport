package com.passport.service.rpc;

import com.common.util.RPCResult;
import com.passport.rpc.AdminRPCService;
import com.passport.rpc.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@com.alibaba.dubbo.config.annotation.Service(version = "1.0.0")
public class AdminRPCServiceImpl implements AdminRPCService {


    @Override
    public RPCResult<UserDTO> login(String loginName, String password) {
        return null;
    }

    @Override
    public RPCResult<List<String>> queryAdminRoles(String pin) {
        return null;
    }
}

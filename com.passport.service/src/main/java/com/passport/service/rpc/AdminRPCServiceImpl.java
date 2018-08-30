package com.passport.service.rpc;

import com.common.util.BeanCoper;
import com.common.util.RPCResult;
import com.passport.domain.AdminUserInfo;
import com.passport.rpc.AdminRPCService;
import com.passport.rpc.dto.UserDTO;
import com.passport.service.AdminUserInfoService;
import com.passport.service.constant.CodeConstant;
import com.passport.service.constant.MessageConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
@com.alibaba.dubbo.config.annotation.Service(version = "1.0.0")
public class AdminRPCServiceImpl implements AdminRPCService {

    @Value("${app.passKey}")
    private String passKey;

    @Resource
    private AdminUserInfoService adminUserInfoService;

    @Override
    public RPCResult<UserDTO> login(String loginName, String password) {
        AdminUserInfo adminInfo = adminUserInfoService.login(loginName, password);
        RPCResult<UserDTO> rpcResult = new RPCResult<>();
        if (adminInfo == null) {
            rpcResult.setCode(CodeConstant.USER_NULL);
            rpcResult.setMessage(MessageConstant.USER_NULL);
            rpcResult.setSuccess(false);
        } else {
            UserDTO userDTO = new UserDTO();
            BeanCoper.copyProperties(userDTO, adminInfo);
            rpcResult.setSuccess(true);
            rpcResult.setData(userDTO);
        }
        return rpcResult;
    }

    @Override
    public RPCResult<List<String>> queryAdminRoles(String pin) {
        AdminUserInfo adminInfo = adminUserInfoService.findByPin(pin);
        RPCResult<List<String>> rpcResult = new RPCResult<>();
        if (adminInfo == null) {
            rpcResult.setCode(CodeConstant.USER_NULL);
            rpcResult.setMessage(MessageConstant.USER_NULL);
            rpcResult.setSuccess(false);
        } else {
            Long[] roles = adminInfo.getRoles();
            List<String>stringList=null;
            if(roles!=null&&roles.length!=0){
                stringList=new ArrayList<>();
                for(Long role:roles){
                    String roleId=String.valueOf(role);
                    if(!stringList.contains(roleId)){
                        stringList.add(roleId);
                    }
                }
            }
            rpcResult.setSuccess(true);
            rpcResult.setData(stringList);
        }
        return rpcResult;
    }
}

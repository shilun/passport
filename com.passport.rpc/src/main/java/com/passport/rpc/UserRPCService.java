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
     * 注册用户
     * @param upPin
     * @param pin
     * @param pass
     * @return
     */
    public RPCResult<UserDTO> regist(String upPin, String pin, String pass,String phone);
    /**
     * 用户登录
     *
     * @param account
     * @param pass
     * @return
     */
    RPCResult<UserDTO> login(String account, String pass);

    /**
     * 用户登录
     * @param token
     * @return
     */
    public RPCResult<UserDTO> loginOut(String token);

    /**
     * 根据用户pin查找用户
     *
     * @param pin
     * @return
     */
    RPCResult<UserDTO> findByPin(String pin);

    /**
     * 强制修改密码
     *
     * @param pin
     * @param pass
     * @return
     */

    RPCResult<Boolean> changePass(String pin, String pass);


    /**
     * 验证用户token
     * @param token
     * @return
     */
    RPCResult<UserDTO> verificationToken(String token);

    /**
     * @param token
     * @param oldPass
     * @param newPass
     * @return
     */

    RPCResult<Boolean> changePass(String token, String oldPass, String newPass);

    /**
     * 修改电话号码
     * @param pin
     * @param phone
     * @return
     */
    RPCResult<Boolean> changePhone(String pin,String phone);

    /**
     * 修改电话号码
     * @param pin
     * @param sexType 1 boy 2 girl
     * @return
     */
    RPCResult<Boolean> changeSexType(String pin,Integer sexType);
    /**
     * 修改电话号码
     * @param pin
     * @param nickName
     * @return
     */
    RPCResult<Boolean> changeNickName(String pin,String nickName);
    /**
     * 修改电话号码
     * @param pin
     * @param sign
     * @return
     */
    RPCResult<Boolean> changeSign(String pin,String sign);
}

package com.passport.rpc;

import com.common.util.RPCResult;
import com.passport.rpc.dto.UserDTO;

/**
 * 客户webservice接口
 * Created by shilun on 16-12-5.
 */
public interface UserRPCService {

    /***
     * 注册
     * @param account 手机或邮箱
     * @return
     */
    RPCResult<String> regist(String account);

    /**
     * 验证注册
     *
     * @param account 手机或邮箱
     * @param vcode   验证码
     * @return
     */
    RPCResult<UserDTO> registVerification(String account, String vcode);

    /**
     * 根据pin查询用户
     *
     * @param pin
     * @return
     */
    RPCResult<UserDTO> findByPin(String pin);

    /***
     * 根据账户查找账户
     * @param account 手机或邮箱
     * @return
     */
    RPCResult<UserDTO> findByAccount(String account);


    /**
     * 构建手机或邮箱登录验证短信
     *
     * @param account 邮箱或手机
     * @return
     */
    RPCResult<Boolean> loginCodeBuild(String account);

    /**
     * 验证码登录验证
     *
     * @param account 手机或邮箱
     * @param vcode
     * @return
     */
    RPCResult<UserDTO> loginCodeBuildVerification(String account, String vcode);

    /***
     *密码登录
     * @param account 手机或邮箱
     * @param passwrd
     * @return
     */
    RPCResult<UserDTO> login(String account, String passwrd);

    /**
     * 初始化密码
     *
     * @param account
     * @param passwd
     * @return
     */
    RPCResult<Boolean> initPass(String account, String passwd);

    /**
     * 修改手机号码发送验证码
     *
     * @param pin    修改用户手机号码生成短信
     * @param mobile
     * @return
     */
    RPCResult<Boolean> changeMobileBuildMsg(String pin, String mobile);


    /**
     * 绑定手机--生成短信验证码
     *
     * @param pin
     * @param mobile 手机
     * @return
     */
    RPCResult<Boolean> bindMobileBuildMsg(String pin, String mobile);

    /**
     * 绑定手机号
     *
     * @param pin
     * @param mobile 手机号码
     * @param msg    短信
     * @return
     */
    RPCResult<Boolean> bindMobile(String pin, String mobile, String msg);

    /**
     * 修改手机
     *
     * @param pin
     * @param mobile
     * @param msg
     * @return
     */
    RPCResult<Boolean> changeMobile(String pin, String mobile, String msg);

    /**
     * 密码修改
     *
     * @param pin
     * @param oldPass
     * @param newPass
     * @return
     */
    RPCResult<Boolean> changePass(String pin, String oldPass, String newPass);

    /**
     * 手机密码修改
     *
     * @param pin
     * @param mobile
     * @param password
     * @param msg
     * @return
     */
    RPCResult<Boolean> changePassByMobile(String pin, String mobile, String msg, String password);

    /***
     * 修改手机号码
     * @param pin
     * @param mobile
     * @return
     */
    RPCResult<Boolean> changePassByMobileBuildMsg(String pin, String mobile);

    /**
     * 密码丢失
     *
     * @param pin
     * @return
     */
    RPCResult<Boolean> forgetPass(String pin);


    /**
     * 密码丢失邮箱校验码验证
     *
     * @param pin
     * @param code
     * @return
     */
    RPCResult<Boolean> forgetPassCodeVerification(String pin, String code, String pass);

    /**
     * 改变用户昵称
     *
     * @param pin
     * @param nickName
     * @return
     */
    RPCResult<Boolean> changeNickName(String pin, String nickName);

    /**
     * 修改用户性别
     *
     * @param pin
     * @param sexType
     * @return
     */
    RPCResult<Boolean> changeSex(String pin, Integer sexType);

    /**
     * 改变用户生日
     *
     * @param pin
     * @param date
     * @return
     */
    RPCResult<Boolean> changeBirthday(String pin, String date);


}

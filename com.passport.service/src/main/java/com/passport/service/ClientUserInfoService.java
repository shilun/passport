package com.passport.service;

import com.common.mongo.MongoService;
import com.common.util.model.SexEnum;
import com.passport.domain.ClientUserInfo;
import com.passport.rpc.dto.UserDTO;
import com.passport.rpc.dto.UserExtendDTO;
import com.passport.service.constant.ChangeType;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

/**
 * 客户用户接口
 */
public interface ClientUserInfoService extends MongoService<ClientUserInfo> {
    /**
     * 登录
     *@param proxyId 代理iD
     * @param loginName
     * @param passwd
     * @return
     */
    ClientUserInfo login(Long proxyId,String loginName, String passwd);

    /**
     * @param pin
     * @return
     */
    ClientUserInfo findByPin(Long proxyId,String pin);

    /**
     *  *@param proxyId 代理iD
     * @param phone
     * @return
     */
    ClientUserInfo findByPhone(Long proxyId,String phone);

    /**
     * 修改用户密码
     *
     * @param pin
     * @param pwd
     */
    void changePass(Long proxyId,String pin, String pwd);

    /***
     * 注册
     * @param account 手机或邮箱
     * @return
     */
    void regist(Long proxyId,String account);

    /**
     * 验证注册
     *
     * @param account 手机或邮箱
     * @param vcode   验证码
     * @return
     */
    UserDTO registVerification(Long proxyId, String account, String vcode, String pass);




    /**
     * 构建手机或邮箱登录验证短信
     *
     * @param account 邮箱或手机
     * @return
     */
    void loginCodeBuild(Long proxyId,String account);

    /**
     * 验证码登录验证
     *
     * @param account 手机或邮箱
     * @param vcode
     * @return
     */
    UserDTO loginCodeBuildVerification(String ip,Long proxyId,String account, String vcode);

    /***
     *密码登录
     * @param account 手机或邮箱
     * @param passwrd
     * @return
     */
    UserDTO login(String ip,Long proxyId,String account, String passwrd);


    /**
     * 初始化密码
     *
     * @param pin
     * @param passwd
     * @return
     */
    void initPass(Long proxyId,String pin, String passwd);

    /**
     * 修改手机号码发送验证码
     *
     * @param pin    修改用户手机号码生成短信
     * @param mobile
     * @return
     */
    void changeMobileBuildMsg(Long proxyId,String pin, String mobile);


    /**
     * 绑定手机--生成短信验证码
     *
     * @param pin
     * @param mobile 手机
     * @return
     */
    void changeMobile(Long proxyId,String pin, String mobile);

    /**
     * 绑定手机号 发送短信
     *
     * @param pin
     * @param mobile 手机号码
     * @return
     */
    void bindMobile(Long proxyId,String pin, String mobile);

    /**
     * 绑定手机号
     *
     * @param pin
     * @param mobile 手机号码
     * @param msg    短信
     * @return
     */
    void bindMobile(Long proxyId,String pin, String mobile, String msg);

    /**
     * 修改手机
     *
     * @param pin
     * @param mobile
     * @param msg
     * @return
     */
    void changeMobile(Long proxyId,String pin, String mobile, String msg);

    /**
     * 密码修改
     *
     * @param pin
     * @param oldPass
     * @param newPass
     * @return
     */
    void changePass(Long proxyId,String pin, String oldPass, String newPass);

    /**
     * 手机密码修改
     *
     * @param pin
     * @param mobile
     * @param password
     * @param msg
     * @return
     */
    void changePassByMobile(Long proxyId,String pin, String mobile, String msg, String password);

    /***
     * 修改手机号码
     * @param pin
     * @param mobile
     * @return
     */
    void changePassByMobileBuildMsg(Long proxyId,String pin, String mobile);

    /**
     * 密码丢失
     *
     * @param pin
     * @return
     */
    void forgetPass(Long proxyId,String pin);


    /**
     * 密码丢失邮箱校验码验证
     *
     * @param pin
     * @param code
     * @return
     */
    UserDTO forgetPassCodeVerification(Long proxyId,String pin, String code, String pass);

    /**
     * 改变用户昵称
     *
     * @param pin
     * @param nickName
     * @return
     */
    void changeNickName(Long proxyId,String pin, String nickName);

    /**
     * 修改用户性别
     *
     * @param pin
     * @param sexType
     * @return
     */
    void changeSex(Long proxyId,String pin, Integer sexType);

    /**
     * 改变用户生日
     *
     * @param pin
     * @param date
     * @return
     */
    void changeBirthday(Long proxyId,String pin, String date);

    /**
     * 保存用户信息
     * @param proxyId
     * @param userExtendDTO
     */
    void saveUserExtendInfo(Long proxyId,UserExtendDTO userExtendDTO);


    /**
     * 退出登录
     * @param token
     */
    void loginOut(String pin,String token);

    Page<ClientUserInfo> QueryRegisterUsers(Integer pageNum, Date startTime, Date endTime);

    /***
     * 注册
     * @param
     * @return
     */
    UserDTO regist(Long proxyId, String account, String pass, String phone, String nick, String email, SexEnum sexEnum,String birth);

    void proxyChangeUserInfo(Long proxyId, String userAccount, ChangeType type,String value);

    Page<ClientUserInfo> proxyGetUsers(Long proxyId,Integer pageNum);
}

package com.passport.service;

import com.common.mongo.MongoService;
import com.common.util.model.SexEnum;
import com.passport.domain.ClientUserInfo;
import com.passport.rpc.dto.ProxyDto;
import com.passport.rpc.dto.UserDTO;
import com.passport.service.constant.ChangeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Map;

/**
 * 客户用户接口
 */
public interface ClientUserInfoService extends MongoService<ClientUserInfo> {
    /**
     * 登录
     *
     * @param proxyId   代理iD
     * @param loginName
     * @param passwd
     * @return
     */
    ClientUserInfo login(Long proxyId, String loginName, String passwd, String ip);

    /**
     * @param pin
     * @return
     */
    ClientUserInfo findByPin(Long proxyId, String pin);

    /**
     * *@param proxyId 代理iD
     *
     * @param phone
     * @return
     */
    ClientUserInfo findByPhone(Long proxyId, String phone);

    /**
     * *@param proxyId 代理iD
     *
     * @param nickName
     * @return
     */
    Page<ClientUserInfo> queryByNick(Long proxyId, String nickName, Pageable pageable);

    /**
     * 修改用户密码
     *
     * @param pin
     * @param pwd
     */
    void changePass(Long proxyId, String pin, String pwd);

    /***
     * 注册
     * @param account 手机或邮箱
     * @return
     */
    void regist(Long proxyId, String account, String refPin);

    /***
     * 注册
     * @param
     * @return
     */
    UserDTO regist(Long proxyId, String refPin, String pass, String phone);

    UserDTO registGuest(Long proxyId, String deviceId, String refPin);

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
    void loginCodeBuild(Long proxyId, String account);

    /**
     * 验证码登录验证
     *
     * @param account 手机或邮箱
     * @param vcode
     * @return
     */
    UserDTO loginCodeBuildVerification(String ip, Long proxyId, String account, String vcode);

    /***
     *密码登录
     * @param account 手机或邮箱
     * @param password
     * @return
     */
    UserDTO login(String ip, Long proxyId, String account, String password);


    /**
     * 初始化密码
     *
     * @param pin
     * @param passwd
     * @return
     */
    void initPass(Long proxyId, String pin, String passwd);

    /**
     * 修改手机号码发送验证码
     *
     * @param pin    修改用户手机号码生成短信
     * @param mobile
     * @return
     */
    void changeMobileBuildMsg(Long proxyId, String pin, String mobile);


    /**
     * 绑定手机--生成短信验证码
     *
     * @param pin
     * @param mobile 手机
     * @return
     */
    void changeMobile(Long proxyId, String pin, String mobile);

    /**
     * 绑定手机号 发送短信
     *
     * @param pin
     * @param mobile 手机号码
     * @return
     */
    void bindMobile(Long proxyId, String pin, String mobile);

    /**
     * 绑定手机号
     *
     * @param pin
     * @param mobile 手机号码
     * @param msg    短信
     * @return
     */
    void bindMobile(Long proxyId, String pin, String mobile, String msg);

    /**
     * 修改手机
     *
     * @param pin
     * @param mobile
     * @param msg
     * @return
     */
    void changeMobile(Long proxyId, String pin, String mobile, String msg);

    /**
     * 密码修改
     *
     * @param pin
     * @param oldPass
     * @param newPass
     * @return
     */
    void changePass(Long proxyId, String pin, String oldPass, String newPass);

    /**
     * 手机密码修改
     *
     * @param pin
     * @param mobile
     * @param password
     * @param msg
     * @return
     */
    void changePassByMobile(Long proxyId, String pin, String mobile, String msg, String password);

    /***
     * 修改手机号码
     * @param pin
     * @param mobile
     * @return
     */
    void changePassByMobileBuildMsg(Long proxyId, String pin, String mobile);

    /**
     * 密码丢失
     *
     * @param phone
     * @return
     */
    void forgetPass(Long proxyId, String phone);


    /**
     * 密码丢失邮箱校验码验证
     *
     * @param phone
     * @param code
     * @return
     */
    UserDTO forgetPassCodeVerification(Long proxyId, String phone, String code, String pass);

    /**
     * 改变用户昵称
     *
     * @param pin
     * @param nickName
     * @return
     */
    void changeNickName(Long proxyId, String pin, String nickName);

    /**
     * 修改用户性别
     *
     * @param pin
     * @param sexType
     * @return
     */
    void changeSex(Long proxyId, String pin, Integer sexType);

    /**
     * 改变用户生日
     *
     * @param pin
     * @param date
     * @return
     */
    void changeBirthday(Long proxyId, String pin, String date);

    /**
     * 退出登录
     *
     * @param token
     */
    void loginOut(Long proxyId, String pin, String token);

    Page<ClientUserInfo> QueryRegisterUsers(Integer pageNum, Date startTime, Date endTime);


    /**
     * 代理修改下面用户的信息
     *
     * @param proxyId
     * @param userAccount
     * @param type
     * @param value
     */
    void proxyChangeUserInfo(Long proxyId, String userAccount, ChangeType type, String value);

    /**
     * 代理获取下面的用户
     *
     * @param proxyId
     * @param pageNum
     * @return
     */
    Page<ClientUserInfo> proxyGetUsers(Long proxyId, Integer pageNum);

    /**
     * 根据注册时间段查询用户列表
     *
     * @param proxyId
     * @param startRegTime
     * @param endRegTime
     * @return
     */
    Page<ClientUserInfo> queryByRegTime(Long proxyId, Date startRegTime, Date endRegTime, Pageable pageable);

    /**
     * 根据注册时间段查询用户数量
     *
     * @param proxyId
     * @param startRegTime
     * @param endRegTime
     * @return
     */
    Long queryCountByRegTime(Long proxyId, Date startRegTime, Date endRegTime);


    UserDTO loginByDeviceUid(Long proxyId, String deviceId);
}

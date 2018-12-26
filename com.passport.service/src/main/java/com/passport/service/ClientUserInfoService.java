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
     *@param proxyId 代理iD
     * @param loginName
     * @param passwd
     * @return
     */
    ClientUserInfo login(Long proxyId,String loginName, String passwd,String ip);

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
     *  *@param proxyId 代理iD
     * @param nickName
     * @return
     */
    Page<ClientUserInfo> queryByNick(Long proxyId,String nickName,Pageable pageable);

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
    UserDTO registVerification(ProxyDto proxydto, String account, String vcode, String pass, String ip);


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
     * @param password
     * @return
     */
    UserDTO login(String ip,Long proxyId,String account, String password);


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
     * @param phone
     * @return
     */
    void forgetPass(Long proxyId,String phone);


    /**
     * 密码丢失邮箱校验码验证
     *
     * @param phone
     * @param code
     * @return
     */
    UserDTO forgetPassCodeVerification(Long proxyId,String phone, String code, String pass);

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
     * 退出登录
     * @param token
     */
    void loginOut(Long proxyId,String pin,String token);

    Page<ClientUserInfo> QueryRegisterUsers(Integer pageNum, Date startTime, Date endTime);

    /***
     * 注册
     * @param
     * @return
     */
    UserDTO regist(ProxyDto proxydto, String recommendId,String refId, String pass, String phone, String nick, String email,
                   SexEnum sexEnum, String birth,String ip,String headUrl,String wechat,String idCard,
                   String realName,Long qq);

    /**
     * 代理修改下面用户的信息
     * @param proxyId
     * @param userAccount
     * @param type
     * @param value
     */
    void proxyChangeUserInfo(Long proxyId, String userAccount, ChangeType type,String value);

    /**
     * 代理获取下面的用户
     * @param proxyId
     * @param pageNum
     * @return
     */
    Page<ClientUserInfo> proxyGetUsers(Long proxyId,Integer pageNum);

    /**
     * 根据注册时间段查询用户列表
     * @param proxyId
     * @param startRegTime
     * @param endRegTime
     * @return
     */
    Page<ClientUserInfo> queryByRegTime(Long proxyId,Date startRegTime,Date endRegTime,Pageable pageable);
    /**
     * 根据注册时间段查询用户数量
     * @param proxyId
     * @param startRegTime
     * @param endRegTime
     * @return
     */
    Long queryCountByRegTime(Long proxyId,Date startRegTime,Date endRegTime);


    /**************以前得接口****************/
    /**
     * 修改密码
     * @param proxyId
     * @param pin
     * @param pwd
     * @param newPwd
     * @return
     */
    Map<String,Object> oldUpdatePwd(Long proxyId,String pin,String pwd,String newPwd);

    /**
     * 忘记密码
     * @param proxyId
     * @param account
     * @param code
     * @param pwd
     * @return
     */
    Map<String,Object> oldForgetPass(Long proxyId, String account, String code,String pwd);

    /**
     * 根据usercode获取用户
     * @param proxyId
     * @param pin
     * @return
     */
    Map<String,Object> oldFindByUserCode(Long proxyId, String pin);

    /**
     * 根据账号获取用户
     * @param proxyId
     * @param account
     * @return
     */
    Map<String,Object> oldFindByAccount(Long proxyId, String account);

    /**
     * 编辑信息
     * @param proxyId
     * @param pin
     * @param nick
     * @param qq
     * @param wechat
     * @param sex
     * @param sign
     * @return
     */
    Map<String,Object> oldEditUserInfo(Long proxyId, String pin,String nick,String qq,String wechat,String sex,String sign);

    /**
     * 实名认证
     * @param proxyId
     * @param pin
     * @param realName
     * @param idCard
     * @return
     */
    Map<String,Object> OldCertification(Long proxyId, String pin,String realName,String idCard);

    /**
     * 注册
     * @param proxydto
     * @param account
     * @param vcode
     * @param pass
     * @param ip
     * @param recommendId  推荐人pin
     * @return
     */
    Map<String, Object> oldRegist(ProxyDto proxydto, String account, String vcode, String pass, String ip,String recommendId);

    /**
     * 注册验证码
     * @param proxyId
     * @param phone
     * @return
     */
    Map<String, Object> oldRegistBuildCode(Long proxyId,String phone);
    /**
     * 忘记密码验证码
     * @param proxyId
     * @param phone
     * @return
     */
    Map<String, Object> oldForgetPassBuildCode(Long proxyId,String phone);

    /**
     * 根据第三方账号登陆
     * @param proxyId
     * @param code
     * @return
     */
    UserDTO wxLogin(Long proxyId,String ip,String code,String nick,String headImg,Integer sex);
}

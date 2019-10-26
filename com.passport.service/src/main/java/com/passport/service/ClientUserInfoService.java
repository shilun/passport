package com.passport.service;

import com.common.mongo.MongoService;
import com.passport.domain.ClientUserInfo;

/**
 * 客户用户接口
 */
public interface ClientUserInfoService extends MongoService<ClientUserInfo> {
    /**
     * 登录
     *
     * @param loginName
     * @param passwd
     * @return
     */
    ClientUserInfo login(String loginName, String passwd);

    /**
     * @param pin
     * @return
     */
    ClientUserInfo findByPin(String pin);

    /**
     * 初始化用户密码
     *
     * @param pin
     * @param pwd
     */
    void initPass(String pin, String pwd);
    /**
     * 密码修改
     *
     * @param pin
     * @param oldPass
     * @param newPass
     * @return
     */
    void changePass(String pin, String oldPass, String newPass);

    /***
     * 注册
     * @param
     * @return
     */
    ClientUserInfo regist(String upPin, String pin, String pass,String phone);




}

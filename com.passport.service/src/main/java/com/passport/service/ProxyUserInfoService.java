package com.passport.service;

import com.common.mongo.MongoService;
import com.passport.domain.ProxyInfo;
import com.passport.domain.ProxyUserInfo;
import com.passport.rpc.dto.ProxyUserDto;

public interface ProxyUserInfoService extends MongoService<ProxyUserInfo> {
    /**
     * 登录
     *
     * @param domain
     * @param account
     * @param pass
     * @return
     */
    ProxyUserInfo login(String domain, String account, String pass);

    /** 添加用户
     * @param proxyId
     * @param phone
     * @param pass
     * @param desc
     * @param resources
     */
    void addUser(String proxyId, String phone, String pass, String desc, String[] resources);

    /**
     * 登出
     * @param token
     */
    void logOut(String token);

    /**
     * 验证用户
     * @param token
     * @return
     */
    ProxyUserDto verfiyToken(String token);

    /**
     * 初始用户密码
     * @param domain
     * @param pin
     * @param pass
     */
    void initPass(String domain, String pin, String pass);

    /**
     * 修改密码
     * @param token
     * @param oldPass
     * @param newPass
     */
    void changePass(String token, String oldPass, String newPass);

    /**
     * 修改用户
     * @param proxyId
     * @param pin
     * @param phone
     * @param desc
     * @param resources
     * @param status
     */
    void upUser(String proxyId, String pin, String phone, String desc, String[] resources, Integer status);
}

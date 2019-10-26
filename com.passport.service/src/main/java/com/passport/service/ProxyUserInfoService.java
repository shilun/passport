package com.passport.service;

import com.common.mongo.MongoService;
import com.passport.domain.ProxyUserInfo;

public interface ProxyUserInfoService extends MongoService<ProxyUserInfo> {

    /**
     * 根据代理商和pin查找用户
     *
     * @param proxyId
     * @param pin
     * @return
     */
    ProxyUserInfo findByProxyIdAndPin(String proxyId, String pin);

    /**
     * 登录
     *
     * @param domain
     * @param account
     * @param pass
     * @return
     */
    ProxyUserInfo login(String domain, String account, String pass);

    /**
     * 添加用户
     *
     * @param proxyId
     * @param pin
     * @param phone
     * @param pass
     * @param desc
     * @param resources
     */
    void addUser(String proxyId, String pin, String phone, String pass, String desc, String[] resources);

    /**
     * 初始用户密码
     *
     * @param proxyId
     * @param pin
     * @param pass
     */
    void initPass(String proxyId, String pin, String pass);

    /**
     * 修改用户
     *
     * @param proxyId
     * @param pin
     * @param phone
     * @param desc
     * @param resources
     * @param status
     */
    void upUser(String proxyId, String pin, String phone, String desc, String[] resources, Integer status);
}

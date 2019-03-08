package com.passport.service;

import com.common.mongo.MongoService;
import com.passport.domain.ProxyUserInfo;

/**
 * @desc 代理商用户修改
 */
public interface ProxyUserInfoService extends MongoService<ProxyUserInfo> {

    /**
     * 登录
     *
     * @param proxyId
     * @param account
     * @param pass
     * @return
     */
    ProxyUserInfo login(Long proxyId, String account, String pass);

    /**
     * @param id
     * @param password
     * @param vpassword
     */
    void changePass(Long id, String password, String vpassword);

    /**
     *
     * @param proxyId
     * @param id
     * @param newpass
     */
    void changePass(Long proxyId, long id, String newpass);


    /**
     * 修改用户角色
     *
     * @param proxyId
     * @param id
     * @param roles
     * @return
     */
    void changeRole(Long proxyId, Long id, Long roles[]);


    /**
     * 修改用户信息
     *
     * @param proxyId
     * @param id
     * @param phone
     * @param desc
     * @param status
     */
    void upUser(Long proxyId, Long id, String phone, String desc, Integer status);

    /**
     * 修改用户信息
     * @param proxyId
     * @param id
     * @param phone
     * @param desc
     * @param status
     * @param roles
     */
    void upUser(Long proxyId, Long id, String phone, String desc, Integer status,Long[]roles);

    /**
     * 添加用户
     *
     * @param proxyId 代理商
     * @param phone   电话
     * @param pass    密码
     * @param desc    备注
     */
    Long addUser(Long proxyId, String phone, String pass, String desc);

    /**
     * 删除用户
     * @param proxyId
     * @param id
     */
    void delById(Long proxyId, Long id);
}
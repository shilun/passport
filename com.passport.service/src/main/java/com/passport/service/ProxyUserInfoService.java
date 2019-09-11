package com.passport.service;

import com.common.mongo.MongoService;
import com.passport.domain.ProxyUserInfo;

/**
 * @desc 代理商用户修改
 */
public interface ProxyUserInfoService extends MongoService<ProxyUserInfo> {

    ProxyUserInfo findByPin(String pin);
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
     * 修改密码
     * @param pin
     * @param oldPass
     * @param newPass
     */
    void changePass(String pin, String oldPass, String newPass);


    void changePass(String pin,String newPass);

    /**
     * 修改用户角色
     *
     * @param proxyId
     * @param id
     * @param roles
     * @return
     */
    void changeRole(Long proxyId, String id, String roles[]);


    void upUser(String pin, String phone, String desc, Integer status,String[]roles);

    /**
     * 添加用户
     *  @param proxyId 代理商
     * @param phone   电话
     * @param pass    密码
     * @param desc    备注
     */
    String addUser(Long proxyId, String phone, String pass, String desc);

    /**
     * 删除用户
     * @param proxyId
     * @param id
     */
    void delById(Long proxyId, String id);

    /**
     * 查询用户
     * @param userId
     * @return
     */
    ProxyUserInfo findBySeqId(Long userId);
}
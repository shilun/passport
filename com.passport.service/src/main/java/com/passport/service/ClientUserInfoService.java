package com.passport.service;

import com.common.mongo.MongoService;
import com.passport.domain.ClientUserInfo;

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
    ClientUserInfo findByPin(String pin);

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
    void changePass(String pin, String pwd);

}

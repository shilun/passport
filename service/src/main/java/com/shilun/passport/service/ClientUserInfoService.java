package com.shilun.passport.service;

import com.common.mongo.MongoService;
import com.shilun.passport.domain.ClientUserInfo;

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
     * @param phone
     * @return
     */
    ClientUserInfo findByPhone(String phone);

    /**
     * 修改用户密码
     *
     * @param pin
     * @param pwd
     */
    void changePass(String pin, String pwd);

}

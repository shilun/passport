package com.passport.service;

import com.common.mongo.MongoService;
import com.passport.domain.AdminUserInfo;

/**
 * @desc admin_user_info 管理员用户
 */
public interface AdminUserInfoService extends MongoService<AdminUserInfo> {

    /**
     * 管理员登录
     *
     * @param loginName
     * @param passport
     * @return
     */
    AdminUserInfo login(String loginName, String passport);


    /**
     * @param pin
     * @return
     */
    AdminUserInfo findByPin(String pin);


    /**
     * 修改密码
     *
     * @param oldPass 旧密码
     * @param newPass 新密码
     */
    void changePass(String pin, String oldPass, String newPass);

    /**
     * @param id
     * @param password
     */
    void changePass(String id, String password);


    /**
     * 初始化密码
     *
     * @param pin
     * @param pass
     */
    void initPass(String pin, String pass);
}
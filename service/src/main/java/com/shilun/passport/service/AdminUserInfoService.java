package com.shilun.passport.service;

import com.common.mongo.MongoService;
import com.shilun.passport.domain.AdminUserInfo;

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
     * 修改密码
     *
     * @param oldPass 旧密码
     * @param newPass 新密码
     */
    void changePass(String pin, String oldPass, String newPass);


    /**
     * 初始化密码
     * @param pin
     * @param pass
     */
    void initPass(String pin, String pass);
}
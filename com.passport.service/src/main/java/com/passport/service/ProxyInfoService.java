package com.passport.service;

import com.common.mongo.MongoService;
import com.passport.domain.ProxyInfo;

/**
 * @desc admin_user_info 代理商信息
 */
public interface ProxyInfoService extends MongoService<ProxyInfo> {

    ProxyInfo findByLoginName(Long proxyId,String loginName,String pass);
    void changePass(Long proxyId,String loginName,String oldPass,String newPass);
    void changePass(Long proxyId,String newPass);
    String refreshToken(Long proxyId);
}
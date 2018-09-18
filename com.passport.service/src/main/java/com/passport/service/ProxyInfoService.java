package com.passport.service;

import com.common.mongo.MongoService;
import com.passport.domain.ProxyInfo;

/**
 * @desc admin_user_info 代理商信息
 */
public interface ProxyInfoService extends MongoService<ProxyInfo> {

    ProxyInfo findByLoginName(String loginName,String pass);

    Boolean changePass(String loginName,String oldPass,String newPass);
}
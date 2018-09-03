package com.passport.service;

import com.common.mongo.MongoService;
import com.passport.domain.ClientUserExtendInfo;

/**
 * @author Luo
 * @date 2018/9/3 19:43
 */
public interface ClientUserExtendInfoService extends MongoService<ClientUserExtendInfo> {
    ClientUserExtendInfo findByUserCode(Integer userCode);
}

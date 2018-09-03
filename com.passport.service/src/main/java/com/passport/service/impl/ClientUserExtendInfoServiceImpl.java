package com.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.passport.domain.ClientUserExtendInfo;
import com.passport.service.ClientUserExtendInfoService;

/**
 * @author Luo
 * @date 2018/9/3 19:44
 */
public class ClientUserExtendInfoServiceImpl extends AbstractMongoService<ClientUserExtendInfo> implements ClientUserExtendInfoService {
    @Override
    protected Class getEntityClass() {
        return ClientUserExtendInfo.class;
    }

    @Override
    public ClientUserExtendInfo findByUserCode(Integer userCode) {
        ClientUserExtendInfo clientUserExtendInfo = new ClientUserExtendInfo();
        clientUserExtendInfo.setUserCode(userCode);
        return this.findByOne(clientUserExtendInfo);
    }
}

package com.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.common.util.model.YesOrNoEnum;
import com.passport.domain.ClientUserExtendInfo;
import com.passport.service.ClientUserExtendInfoService;
import org.springframework.stereotype.Service;

/**
 * @author Luo
 * @date 2018/9/3 19:44
 */
@Service
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

    @Override
    public Long insert(ClientUserExtendInfo entity) {
        if(entity.getRobot() == null){
            entity.setRobot(YesOrNoEnum.NO.getValue());
        }
        return super.insert(entity);
    }
}

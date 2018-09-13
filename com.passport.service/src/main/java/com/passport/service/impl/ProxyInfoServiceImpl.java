package com.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.common.security.MD5;
import com.common.util.StringUtils;
import com.common.util.model.YesOrNoEnum;
import com.passport.domain.ProxyInfo;
import com.passport.service.ProxyInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProxyInfoServiceImpl extends AbstractMongoService<ProxyInfo> implements ProxyInfoService {

    @Value("${app.passKey}")
    private String passKey;

    @Override
    protected Class getEntityClass() {
        return ProxyInfo.class;
    }

    @Override
    public ProxyInfo findByLoginName(String loginName, String pass) {
        ProxyInfo query = new ProxyInfo();
        query.setStatus(YesOrNoEnum.YES.getValue());
        pass = MD5.MD5Str(pass, passKey);
        query.setPass(pass);
        boolean setLoginName=false;
        if(StringUtils.isMobileNO(loginName)){
            setLoginName=true;
            query.setPhone(loginName);
        }
        if(setLoginName==false){
            query.setPin(loginName);
        }
        query = findByOne(query);
        return query;
    }


    @Override
    public Boolean changePass(String loginName, String oldPass, String newPass) {
        ProxyInfo proxyInfo = this.findByLoginName(loginName, oldPass);
        if(proxyInfo == null){
            return false;
        }
        newPass = MD5.MD5Str(newPass, passKey);
        proxyInfo.setPass(newPass);
        return save(proxyInfo) > 0 ? true:false;
    }
}

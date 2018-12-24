package com.passport.service.impl;

import com.common.exception.BizException;
import com.common.mongo.AbstractMongoService;
import com.common.security.MD5;
import com.common.util.StringUtils;
import com.common.util.model.YesOrNoEnum;
import com.passport.domain.ProxyInfo;
import com.passport.domain.ProxyUserInfo;
import com.passport.service.ProxyInfoService;
import com.passport.service.ProxyUserInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProxyInfoServiceImpl extends AbstractMongoService<ProxyInfo> implements ProxyInfoService {

    @Value("${app.passKey}")
    private String passKey;


    @Resource
    private ProxyUserInfoService proxyUserInfoService;


    @Override
    protected Class getEntityClass() {
        return ProxyInfo.class;
    }

//
//    @Override
//    public String refreshToken(Long proxyId) {
//        ProxyInfo entity = new ProxyInfo();
//        entity.setId(proxyId);
//        entity.setToken(StringUtils.getUUID());
//        up(entity);
//        return entity.getToken();
//    }


    @Override
    public Long save(ProxyInfo entity) {
        if (entity.getId() == null) {
            entity.setToken(StringUtils.getUUID());
        }
        Long save = super.save(entity);
        proxyUserInfoService.addUser(entity.getId(), entity.getPhone(), entity.getPass(), entity.getRemark());
        return save;
    }
//
//    @Override
//    public ProxyInfo findByLoginName(Long proxyId, String loginName, String pass) {
//        ProxyInfo query = new ProxyInfo();
//        query.setStatus(YesOrNoEnum.YES.getValue());
//        query.setId(proxyId);
//        boolean setLoginName = false;
//        if (StringUtils.isMobileNO(loginName)) {
//            setLoginName = true;
//            query.setPhone(loginName);
//        }
//        if (setLoginName == false) {
//            query.setPin(loginName);
//        }
//        query = findByOne(query);
//        pass = MD5.MD5Str(pass, passKey);
//        if (!pass.equals(query.getPass())) {
//            return null;
//        }
//        return query;
//    }
//
//
//    @Override
//    public void changePass(Long proxyId, String loginName, String oldPass, String newPass) {
//        ProxyInfo proxyInfo = this.findByLoginName(proxyId, loginName, oldPass);
//        if (proxyInfo == null) {
//            throw new BizException("oldPass.error", "旧密码错误");
//        }
//        newPass = MD5.MD5Str(newPass, passKey);
//        proxyInfo.setPass(newPass);
//        save(proxyInfo);
//
//    }
//
//    @Override
//    public void changePass(Long proxyId, String newPass) {
//        ProxyInfo proxyInfo = findById(proxyId);
//        if (proxyInfo == null) {
//            throw new BizException("data.error", "数据不存在");
//        }
//        ProxyUserInfo proxyUserInfo = new ProxyUserInfo();
//        proxyUserInfo.setPhone(proxyInfo.getPhone());
//        proxyUserInfo.setProxyId(proxyId);
//        ProxyUserInfo user = proxyUserInfoService.findByOne(proxyUserInfo);
//        Long userId = null;
//        if (user == null) {
//            userId = proxyUserInfoService.addUser(proxyId, proxyInfo.getPhone(), MD5.MD5Str(newPass, passKey), proxyInfo.getRemark());
//        }
//        else{
//            userId=user.getId();
//        }
//        ProxyUserInfo entity = new ProxyUserInfo();
//        entity.setId(userId);
//        entity.setPass(MD5.MD5Str(newPass, passKey));
//        proxyUserInfoService.save(entity);
//
//
//    }
}

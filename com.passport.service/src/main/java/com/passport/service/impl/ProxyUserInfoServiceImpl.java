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
public class ProxyUserInfoServiceImpl extends AbstractMongoService<ProxyUserInfo> implements ProxyUserInfoService {
    @Value("${app.passKey}")
    private String passKey;

    @Override
    protected Class getEntityClass() {
        return ProxyUserInfo.class;
    }

    @Resource
    private ProxyInfoService proxyInfoService;

    @Override
    public Long addUser(Long proxyId, String phone, String pass, String desc) {
        ProxyUserInfo query = new ProxyUserInfo();
        query.setProxyId(proxyId);
        query.setPhone(phone);
        ProxyUserInfo byOne = findByOne(query);
        if (byOne != null) {
            throw new BizException("add.user.error", "添加用户失败,用户重复");
        }
        query.setPass(MD5.MD5Str(pass, passKey));
        query.setDesc(desc);
        query.setStatus(YesOrNoEnum.YES.getValue());
        save(query);
        return query.getId();
    }

    @Override
    public ProxyUserInfo login(Long proxyId, String account, String pass) {
        ProxyUserInfo query = new ProxyUserInfo();
        query.setStatus(YesOrNoEnum.YES.getValue());

        boolean setLoginName = false;
        if (StringUtils.isMobileNO(account)) {
            setLoginName = true;
            query.setPhone(account);
        }
        if (setLoginName == false) {
            query.setPin(account);
        }
        query = findByOne(query);
        pass = MD5.MD5Str(pass, passKey);
        if (!pass.equals(query.getPass())) {
            return null;
        }
        if (query.getStatus().intValue() == YesOrNoEnum.NO.getValue()) {
            return null;
        }

        return query;
    }

    @Override
    public void changePass(Long proxyId, String account, String pass, String newPass) {
        ProxyUserInfo query = new ProxyUserInfo();
        query.setProxyId(proxyId);
        boolean setLoginName = false;
        if (StringUtils.isMobileNO(account)) {
            setLoginName = true;
            query.setPhone(account);
        }
        if (setLoginName == false) {
            query.setPin(account);
        }
        query = findByOne(query);
        pass = MD5.MD5Str(pass, passKey);
        if (!pass.equals(query.getPass())) {
            throw new BizException("old.password.error", "旧密码失败");
        }
        ProxyUserInfo upEntity = new ProxyUserInfo();
        upEntity.setId(query.getId());
        upEntity.setPass(MD5.MD5Str(newPass, passKey));
        save(upEntity);
    }

    @Override
    public void changeRole(Long proxyId, Long id, Long[] roles) {
        ProxyUserInfo entity = findById(id);
        if (entity.getProxyId().longValue() != proxyId) {
            throw new BizException("data.error", "数据失败");
        }
        ProxyInfo proxyInfo = proxyInfoService.findById(proxyId);
        if(proxyInfo.getPhone().equals(entity.getPhone())){
            throw new BizException("data.up.error", "管理员不能修改角色");
        }
        entity = new ProxyUserInfo();
        entity.setId(id);
        entity.setRoles(roles);
        up(entity);
    }


    @Override
    public void upUser(Long proxyId, Long id, String phone, String desc, Integer status) {
        ProxyUserInfo query = new ProxyUserInfo();
        query.setPhone(phone);
        ProxyUserInfo info = findByOne(query);
        if (info != null && info.getId().longValue() != id.longValue()) {
            throw new BizException("up.user.error", "修改用户失败，手机号已存在");
        }
        ProxyUserInfo entity = findById(id);
        if (entity.getProxyId().longValue() != proxyId) {
            throw new BizException("data.error", "数据失败");
        }
        entity = new ProxyUserInfo();
        entity.setId(id);
        entity.setStatus(status);
        entity.setPhone(phone);
        entity.setDesc(desc);
        save(entity);
    }
}

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

    @Override
    protected Class getEntityClass() {
        return ProxyUserInfo.class;
    }

    @Resource
    private ProxyInfoService proxyInfoService;

    @Value("${app.passKey}")
    private String passKey;

    @Override
    public ProxyUserInfo findByProxyIdAndPin(String proxyId, String pin) {
        ProxyUserInfo info = new ProxyUserInfo();
        info.setPin(pin);
        info.setStatus(YesOrNoEnum.YES.getValue());
        info.setProxyId(proxyId);
        return findByOne(info);
    }

    @Override
    public ProxyUserInfo login(String domain, String account, String pass) {
        ProxyInfo proxy = proxyInfoService.findByDomain(domain);
        pass = MD5.MD5Str(pass, passKey);
        ProxyUserInfo info = new ProxyUserInfo();
        info.setPass(pass);
        info.setPin(account);
        info.setStatus(YesOrNoEnum.YES.getValue());
        info.setProxyId(proxy.getId());
        info = findByOne(info);
        if (info == null) {
            throw new BizException("login.error", "登录失败");
        }
        return info;
    }

    @Override
    public void addUser(String proxyId, String pin, String phone, String pass, String desc, String[] resources) {
        if (StringUtils.isBlank(proxyId)) {
            throw new BizException("proxyId.error");
        }
        if (StringUtils.isBlank(pin)) {
            throw new BizException("pin.error");
        }
        if (StringUtils.isBlank(phone)) {
            throw new BizException("phone.error");
        }
        if (StringUtils.isBlank(pass)) {
            throw new BizException("pass.error");
        }
        if (StringUtils.isBlank(desc)) {
            throw new BizException("desc.error");
        }
        ProxyUserInfo entity = new ProxyUserInfo();
        entity.setProxyId(proxyId);
        entity.setPin(pin);
        entity.setPhone(phone);
        entity.setPass(pass);
        entity.setDesc(desc);
        entity.setResources(resources);
        insert(entity);
    }

    @Override
    public void initPass(String proxyId, String pin, String pass) {
        ProxyUserInfo info = findByProxyIdAndPin(proxyId, pin);
        ProxyUserInfo up = new ProxyUserInfo();
        up.setId(info.getId());
        up.setPass(MD5.MD5Str(pass, pass));
        save(up);
    }

    @Override
    public void upUser(String proxyId, String pin, String phone, String desc, String[] resources, Integer status) {
        ProxyUserInfo info = findByProxyIdAndPin(proxyId, pin);
        info.setResources(resources);
        info.setStatus(status);
        info.setPhone(phone);
        info.setDesc(desc);
        save(info);
    }
}

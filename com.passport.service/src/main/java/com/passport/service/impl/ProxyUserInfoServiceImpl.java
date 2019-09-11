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
    public ProxyUserInfo findBySeqId(Long userId) {
        ProxyUserInfo query = new ProxyUserInfo();
        query.setSeqId(userId);
        return findByOne(query);
    }

    @Override
    public ProxyUserInfo findByPin(String pin) {
        ProxyUserInfo query = new ProxyUserInfo();
        query.setPin(pin);
        return findByOne(query);
    }


    @Override
    public void changePass(String pin, String newPass) {
        ProxyUserInfo query = new ProxyUserInfo();
        query.setPin(pin);
        query = findByOne(query);
        ProxyUserInfo upEntity = new ProxyUserInfo();
        upEntity.setId(query.getId());
        upEntity.setPass(MD5.MD5Str(newPass, passKey));
        save(upEntity);
    }

    @Override
    public void save(ProxyUserInfo entity) {
        if (entity.getId() == null) {
            ProxyUserInfo query = new ProxyUserInfo();
            query.setProxyId(entity.getProxyId());
            if (StringUtils.isNotEmpty(entity.getPin())) {
                query.setPin(entity.getPin());
            }
            ProxyUserInfo byOne = findByOne(query);
            if (byOne != null) {
                throw new BizException("add.user.error", "添加用户失败,用户重复");
            }
            if (StringUtils.isNotEmpty(entity.getPhone())) {
                query.setPhone(entity.getPhone());
            }
            byOne = findByOne(query);
            if (byOne != null) {
                throw new BizException("add.user.error", "添加用户失败,电话重复");
            }
        }

        super.save(entity);
    }


    @Override
    public void delById(Long proxyId, String pin) {
        ProxyUserInfo entity = findByPin(pin);
        if (entity.getProxyId().longValue() != proxyId) {
            throw new BizException("data.error", "数据失败");
        }
        delById(entity.getId());
    }

    @Override
    public String addUser(Long proxyId, String phone, String pass, String desc) {
        ProxyUserInfo query = new ProxyUserInfo();
        query.setProxyId(proxyId);
        query.setPhone(phone);
        ProxyUserInfo byOne = findByOne(query);
        if (byOne != null) {
            throw new BizException("add.user.error", "添加用户失败,用户重复");
        }
        query.setPin(phone);
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
            return null;
        }
        if (query.getStatus().intValue() == YesOrNoEnum.NO.getValue()) {
            return null;
        }

        return query;
    }

    @Override
    public void changePass(String pin, String password, String vpassword) {
        ProxyUserInfo query = new ProxyUserInfo();
        query.setPin(pin);
        query = findByOne(query);
        if (query == null) {
            throw new BizException("user.password.error", "用户不存在");
        }
        if (!password.equals(vpassword)) {
            throw new BizException("user.password.error", "密码和验证密码不一至");
        }
        ProxyUserInfo upEntity = new ProxyUserInfo();
        upEntity.setId(query.getId());
        upEntity.setPass(MD5.MD5Str(password, passKey));
        save(upEntity);
    }

    @Override
    public void changeRole(Long proxyId, String id, String[] roles) {
        ProxyUserInfo entity = findById(id);
        if (entity.getProxyId().longValue() != proxyId) {
            throw new BizException("data.error", "数据失败");
        }
        ProxyInfo proxyInfo = proxyInfoService.findBySeqId(proxyId);
        if (proxyInfo.getPhone().equals(entity.getPhone())) {
            throw new BizException("data.up.error", "管理员不能修改角色");
        }
        entity = new ProxyUserInfo();
        entity.setId(id);
        entity.setRoles(roles);
        up(entity);
    }


    @Override
    public void upUser(String pin, String phone, String desc, Integer status, String[] roles) {
        ProxyUserInfo query = new ProxyUserInfo();
        query.setPhone(phone);
        ProxyUserInfo entity = findByOne(query);
        String id = entity.getId();
        if (entity != null && !entity.getPin().equalsIgnoreCase(pin)) {
            throw new BizException("up.user.error", "修改用户失败，手机号已存在");
        }
        entity = new ProxyUserInfo();
        entity.setId(id);
        entity.setStatus(status);
        entity.setPhone(phone);
        entity.setDesc(desc);
        entity.setRoles(roles);
        save(entity);
    }

}

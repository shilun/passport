package com.passport.service.impl;

import com.common.exception.BizException;
import com.common.mongo.AbstractMongoService;
import com.common.security.MD5;
import com.common.util.StringUtils;
import com.common.util.model.YesOrNoEnum;
import com.passport.domain.AdminUserInfo;
import com.passport.service.AdminUserInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminUserInfoServiceImpl extends AbstractMongoService<AdminUserInfo> implements AdminUserInfoService {
    @Value("${app.passKey}")
    private String passKey;

    @Override
    protected Class getEntityClass() {
        return AdminUserInfo.class;
    }

    @Override
    public AdminUserInfo login(String loginName, String passport) {
        AdminUserInfo query = new AdminUserInfo();
        query.setStatus(YesOrNoEnum.YES.getValue());
        passport = MD5.MD5Str(passport, passKey);
        query.setPasswd(passport);
        query.setPin(loginName);
        AdminUserInfo info = findByOne(query);
        return info;
    }

    public AdminUserInfo findByPin(String pin) {
        AdminUserInfo query = new AdminUserInfo();
        query.setPin(pin);
        query.setDelStatus(YesOrNoEnum.NO.getValue());
        return findByOne(query);
    }


    @Override
    public void changePass(String pin, String oldPass, String newPass) {
        AdminUserInfo query = new AdminUserInfo();
        query.setStatus(YesOrNoEnum.YES.getValue());
        oldPass = MD5.MD5Str(oldPass, passKey);
        query.setPasswd(oldPass);
        query.setPin(pin);
        AdminUserInfo info = findByOne(query);
        if (info == null) {
            throw new BizException("oldPass.error", "旧密码错误");
        }
        AdminUserInfo entity = new AdminUserInfo();
        entity.setId(info.getId());
        entity.setPasswd(MD5.MD5Str(newPass, passKey));
        up(entity);
    }

    @Override
    public void initPass(String pin, String pass) {
        AdminUserInfo query = new AdminUserInfo();
        query.setStatus(YesOrNoEnum.YES.getValue());
        query.setPin(pin);
        AdminUserInfo info = findByOne(query);
        if (info == null) {
            throw new BizException("oldPass.error", "旧密码错误");
        }
        AdminUserInfo entity = new AdminUserInfo();
        entity.setId(info.getId());
        entity.setPasswd(MD5.MD5Str(pass, passKey));
        up(entity);
    }

    @Override
    public Long insert(AdminUserInfo entity) {
        entity.setPin(StringUtils.getUUID());
        return super.insert(entity);
    }
}

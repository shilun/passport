package com.shilun.passport.service.impl;

import com.common.exception.BizException;
import com.common.mongo.AbstractMongoService;
import com.common.security.MD5;
import com.common.util.model.YesOrNoEnum;
import com.shilun.passport.domain.AdminUserInfo;
import com.shilun.passport.service.AdminUserInfoService;
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
}

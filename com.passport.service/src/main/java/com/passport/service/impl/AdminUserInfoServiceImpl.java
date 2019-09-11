package com.passport.service.impl;

import com.common.exception.BizException;
import com.common.mongo.AbstractMongoService;
import com.common.security.MD5;
import com.common.util.StringUtils;
import com.common.util.model.YesOrNoEnum;
import com.passport.domain.AdminUserInfo;
import com.passport.service.AdminUserInfoService;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminUserInfoServiceImpl extends AbstractMongoService<AdminUserInfo> implements AdminUserInfoService {

    private static Logger logger= LoggerFactory.getLogger(AdminUserInfoServiceImpl.class);
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
        logger.error("loginName:"+loginName+" pass:"+passport);
        query.setPasswd(passport);
        boolean setLoginName=false;
        if(StringUtils.isEmail(loginName)){
            query.setEmail(loginName);
            setLoginName=true;
        }
        if(StringUtils.isMobileNO(loginName)){
            setLoginName=true;
            query.setPhone(loginName);
        }
        if(setLoginName==false){
            query.setName(loginName);
        }
        query.setName(loginName);
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
    public void changePass(Long id, String password) {
        AdminUserInfo upEntity = new AdminUserInfo();
        upEntity.setSeqId(id);
        upEntity.setPasswd(MD5.MD5Str(password, passKey));
        up(upEntity);
    }

    @Override
    public void insert(AdminUserInfo entity) {
        entity.setPin(StringUtils.getUUID());
        super.insert(entity);
    }
}

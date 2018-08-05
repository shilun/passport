package com.shilun.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.common.security.MD5;
import com.common.util.StringUtils;
import com.common.util.model.YesOrNoEnum;
import com.shilun.passport.domain.module.UserStatusEnum;
import com.shilun.passport.service.ClientUserInfoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;

@Service
public class ClientUserInfoServiceImpl extends AbstractMongoService<com.shilun.passport.domain.ClientUserInfo> implements ClientUserInfoService {
    private static Logger logger = Logger.getLogger(ClientUserInfoServiceImpl.class);
    private final static String CLIENT_USER_CACHE = "passport.cache.{0}";
    /**
     * 用户session 时间时长
     */
    public final static int USER_SESSION_TIME = 60 * 60 * 24 * 30;
    @Value("${app.passKey}")
    private String passKey;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    protected Class<com.shilun.passport.domain.ClientUserInfo> getEntityClass() {
        return com.shilun.passport.domain.ClientUserInfo.class;
    }

    public com.shilun.passport.domain.ClientUserInfo login(String loginName, String passwd) {
        if (StringUtils.isBlank(loginName) || StringUtils.isBlank(passwd)) {
            return null;
        }
        com.shilun.passport.domain.ClientUserInfo query = new com.shilun.passport.domain.ClientUserInfo();
        query.setPasswd(MD5.MD5Str(passwd, passKey));
        query.setStatus(UserStatusEnum.Normal.getValue());
        query.setDelStatus(YesOrNoEnum.NO.getValue());
        boolean account = false;
        if (StringUtils.isMobileNO(loginName)) {
            account = true;
            query.setPhone(loginName);
        }
        if (StringUtils.isEmail(loginName)) {
            account = true;
            query.setEmail(loginName);
        }
        if (!account) {
            query.setPin(loginName);
        }
        query = findByOne(query);
        if (query != null) {
            return query;
        }
        return null;
    }

    public com.shilun.passport.domain.ClientUserInfo findByPin(String pin) {
        String userKey = MessageFormat.format(CLIENT_USER_CACHE, pin);
        com.shilun.passport.domain.ClientUserInfo user = (com.shilun.passport.domain.ClientUserInfo) redisTemplate.opsForValue().get(userKey);
        if (user == null) {
            com.shilun.passport.domain.ClientUserInfo query = new com.shilun.passport.domain.ClientUserInfo();
            query.setPin(pin);
            query.setDelStatus(YesOrNoEnum.NO.getValue());
            user = findByOne(query);
            if (user != null) {
                redisTemplate.opsForValue().set(userKey, user, USER_SESSION_TIME);
            }
            return user;
        } else {
            return user;
        }
    }

    public com.shilun.passport.domain.ClientUserInfo findByPhone(String phone) {
        com.shilun.passport.domain.ClientUserInfo clientUserInfo = new com.shilun.passport.domain.ClientUserInfo();
        clientUserInfo.setPhone(phone);
        clientUserInfo.setDelStatus(YesOrNoEnum.NO.getValue());
        return this.findByOne(clientUserInfo);
    }

    public void changePass(String pin, String pwd) {
        com.shilun.passport.domain.ClientUserInfo byPin = findByPin(pin);
        Long id = byPin.getId();
        com.shilun.passport.domain.ClientUserInfo info = new com.shilun.passport.domain.ClientUserInfo();
        info.setPasswd(MD5.MD5Str(pwd, passKey));
        info.setId(id);
        try {
            String userKey = MessageFormat.format(CLIENT_USER_CACHE, pin);
            redisTemplate.delete(userKey);
        } catch (Exception e) {
            logger.error("删除用户缓存失败", e);
        }
        up(info);
    }
}

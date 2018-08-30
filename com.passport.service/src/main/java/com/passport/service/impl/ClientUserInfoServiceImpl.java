package com.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.common.security.MD5;
import com.common.util.StringUtils;
import com.common.util.model.YesOrNoEnum;
import com.passport.domain.ClientUserInfo;
import com.passport.domain.module.UserStatusEnum;
import com.passport.service.ClientUserInfoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@Service
public class ClientUserInfoServiceImpl extends AbstractMongoService<ClientUserInfo> implements ClientUserInfoService {
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

    protected Class<ClientUserInfo> getEntityClass() {
        return ClientUserInfo.class;
    }

    public ClientUserInfo login(Long proxyId,String loginName, String passwd) {
        if (StringUtils.isBlank(loginName) || StringUtils.isBlank(passwd)||proxyId==null) {
            return null;
        }
        ClientUserInfo query = new ClientUserInfo();
        query.setPasswd(MD5.MD5Str(passwd, passKey));
        query.setStatus(UserStatusEnum.Normal.getValue());
        query.setProxyId(proxyId);
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

    public ClientUserInfo findByPin(String pin) {
        String userKey = MessageFormat.format(CLIENT_USER_CACHE, pin);
        ClientUserInfo user = (ClientUserInfo) redisTemplate.opsForValue().get(userKey);
        if (user == null) {
            ClientUserInfo query = new ClientUserInfo();
            query.setPin(pin);
            query.setDelStatus(YesOrNoEnum.NO.getValue());
            user = findByOne(query);
            if (user != null) {
                redisTemplate.opsForValue().set(userKey, user, USER_SESSION_TIME, TimeUnit.MINUTES);
            }
            return user;
        } else {
            return user;
        }
    }

    public ClientUserInfo findByPhone(Long proxyId,String phone) {
        ClientUserInfo clientUserInfo = new ClientUserInfo();
        clientUserInfo.setProxyId(proxyId);
        clientUserInfo.setPhone(phone);
        return this.findByOne(clientUserInfo);
    }

    public void changePass(String pin, String pwd) {
        ClientUserInfo byPin = findByPin(pin);
        Long id = byPin.getId();
        ClientUserInfo info = new ClientUserInfo();
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

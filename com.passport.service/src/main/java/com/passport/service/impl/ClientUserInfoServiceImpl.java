package com.passport.service.impl;

import com.common.exception.BizException;
import com.common.mongo.AbstractMongoService;
import com.common.security.MD5;
import com.common.util.StringUtils;
import com.common.util.model.YesOrNoEnum;
import com.passport.domain.ClientUserInfo;
import com.passport.service.ClientUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@Service
public class ClientUserInfoServiceImpl extends AbstractMongoService<ClientUserInfo> implements ClientUserInfoService {
    private static Logger logger = LoggerFactory.getLogger(ClientUserInfoServiceImpl.class);

    private final String CLIENT_USER_CACHE = "passport.cache.{0}";
    /**
     * 用户session   时间时长
     */
    public final static int USER_SESSION_TIME = 60 * 24 * 30;
    @Value("${app.passKey}")
    private String passKey;

    @Value("${app.token.encode.key}")
    private String appTokenEncodeKey;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    protected Class<ClientUserInfo> getEntityClass() {
        return ClientUserInfo.class;
    }


    public ClientUserInfo login(Long proxyId, String loginName, String passwd, String ip) {
        if (StringUtils.isBlank(loginName) || StringUtils.isBlank(passwd) || proxyId == null) {
            return null;
        }
        ClientUserInfo query = new ClientUserInfo();
        query.setPasswd(MD5.MD5Str(passwd, passKey));
        query.setStatus(YesOrNoEnum.YES.getValue());
        query.setDelStatus(YesOrNoEnum.NO.getValue());
        boolean account = false;
        if (StringUtils.isMobileNO(loginName)) {
            account = true;
            query.setPhone(loginName);
        }
        if (!account) {
            query.setPin(loginName);
        }
        query = findByOne(query);
        if (query == null) {
            return null;
        }
        return query;
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

    public void changePass(String pin, String pwd) {
        ClientUserInfo byPin = findByPin(pin);
        String id = byPin.getId();
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


    @Override
    public ClientUserInfo login(String loginName, String passwd) {
        ClientUserInfo byPin = findByPin(loginName);
        if (byPin == null) {
            throw new BizException("login.error.user.not.exist");
        }
        if (!byPin.getPasswd().equals(MD5.MD5Str(passwd, passKey))) {
            throw new BizException("login.error.userOrpass.error");
        }
        return byPin;
    }

    @Override
    public ClientUserInfo regist(String upPin, String pin, String pass) {
        if (StringUtils.isNotBlank(upPin)) {
            ClientUserInfo info = new ClientUserInfo();
            info.setPin(upPin);
            ClientUserInfo refInfo = findByOne(info);
            if (refInfo == null) {
                throw new BizException("regist.user.error.upPin.error");
            }
            refInfo.getPins().add(pin);
            up(refInfo);

            info = new ClientUserInfo();
            info.setPin(pin);
            info.setStatus(YesOrNoEnum.YES.getValue());
            info.setPasswd(MD5.MD5Str(pass, passKey));
            info.setUpPin(upPin);
            insert(info);
            return info;
        } else {
            ClientUserInfo info = new ClientUserInfo();
            info.setPin(pin);
            info.setPasswd(MD5.MD5Str(pass, passKey));
            info.setStatus(YesOrNoEnum.YES.getValue());
            insert(info);
            return info;
        }
    }

    @Override
    public void changePass(String pin, String oldPass, String newPass) {
        ClientUserInfo info = new ClientUserInfo();
        info.setPin(pin);
        info = findByOne(info);
        if (info != null) {
            String id=info.getId();
            if(info.getPasswd().equals(MD5.MD5Str(oldPass,passKey))){
                info = new ClientUserInfo();
                info.setId(id);
                info.setPasswd(MD5.MD5Str(newPass,passKey));
                up(info);
            }
            else{
                throw new BizException("changePass.error.oldPass.error");
            }
        }
    }
}

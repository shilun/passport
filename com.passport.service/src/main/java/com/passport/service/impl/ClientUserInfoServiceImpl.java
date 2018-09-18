package com.passport.service.impl;

import com.common.exception.BizException;
import com.common.mongo.AbstractMongoService;
import com.common.security.MD5;
import com.common.util.BeanCoper;
import com.common.util.StringUtils;
import com.common.util.model.SexEnum;
import com.common.util.model.YesOrNoEnum;
import com.passport.domain.ClientUserExtendInfo;
import com.passport.domain.ClientUserInfo;
import com.passport.domain.SMSInfo;
import com.passport.domain.module.UserStatusEnum;
import com.passport.rpc.dto.UserDTO;
import com.passport.rpc.dto.UserExtendDTO;
import com.passport.service.ClientUserExtendInfoService;
import com.passport.service.ClientUserInfoService;
import com.passport.service.SMSInfoService;
import com.passport.service.constant.MessageConstant;
import com.passport.service.constant.SysContant;
import com.passport.service.util.AliyunMnsUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class ClientUserInfoServiceImpl extends AbstractMongoService<ClientUserInfo> implements ClientUserInfoService {
    private static Logger logger = Logger.getLogger(ClientUserInfoServiceImpl.class);

    private final static String CLIENT_USER_CACHE = "passport.cache.{0}";
    private final String PASS_USER_REG = "passport.userrpc.reg.account.{0}.proxyid.{1}";
    private final String LOGIN_MOBILE_CODE = "passport.userrpc.login.account.{0}.proxyid.{1}";
    private final String LOGIN_PIN = "passport.login.{0}";
    private final String LOGIN_PIN_TOKEN = "passport.login.{0}.token.{1}";
    private final String MOBILE_USER_CHANGE = "passport.userrpc.change.mobile.{0}";
    private final String MOBILE_USER_BIND = "passport.userrpc.bind.mobile.{0}";
    private final String PASS_USER_CHANGE_BY_MOBILE = "passport.userrpc.changepass.mobile.{0}";
    private final String FORGET_PASS = "passport.userrpc.forgetpass.pin.{0}";
    /**
     * 用户session 时间时长
     */
    public final static int USER_SESSION_TIME = 60 * 60 * 24 * 30;
    @Value("${app.passKey}")
    private String passKey;

    @Resource
    private SMSInfoService smsInfoService;
    @Resource
    private ClientUserExtendInfoService clientUserExtendInfoService;
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

    public ClientUserInfo findByPin(Long proxyId,String pin) {
        String userKey = MessageFormat.format(CLIENT_USER_CACHE, pin);
        ClientUserInfo user = (ClientUserInfo) redisTemplate.opsForValue().get(userKey);
        if (user == null) {
            ClientUserInfo query = new ClientUserInfo();
            query.setPin(pin);
            query.setProxyId(proxyId);
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

    public void changePass(Long proxyId,String pin, String pwd) {
        ClientUserInfo byPin = findByPin(proxyId,pin);
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

    @Override
    public void regist(Long proxyId, String account) {
        if (StringUtils.isBlank(account)) {
            return ;
        }
        if (!StringUtils.isMobileNO(account)) {
            return ;
        }
        ClientUserInfo entity = findByPhone(proxyId, account);
        if (entity != null) {
            return ;
        }
        String code = AliyunMnsUtil.randomSixCode();
        String redisKey = MessageFormat.format(PASS_USER_REG, account, proxyId);
        sendSMSCode(account, redisKey, code);
    }

    @Override
    public UserDTO registVerification(Long proxyId, String account, String vcode, String pass) {
        try {
            String key = MessageFormat.format(PASS_USER_REG, account, proxyId);
            String o = (String) redisTemplate.opsForValue().get(key);
            if (o.equalsIgnoreCase(vcode)) {
                ClientUserInfo entity = new ClientUserInfo();
                entity.setProxyId(proxyId);
                entity.setPin(StringUtils.getUUID());
                entity.setPhone(account);
                entity.setNickName(account);
                entity.setSexType(SexEnum.MALE.getValue());
                entity.setStatus(YesOrNoEnum.YES.getValue());
                entity.setPasswd(MD5.MD5Str(pass, passKey));
                save(entity);

                ClientUserExtendInfo clientUserExtendInfo = new ClientUserExtendInfo();
                clientUserExtendInfo.setUserCode(entity.getId().intValue());
                clientUserExtendInfo.setRobot(YesOrNoEnum.NO.getValue());
                clientUserExtendInfoService.save(clientUserExtendInfo);

                UserDTO dto = new UserDTO();
                BeanCoper.copyProperties(dto, entity);
                return dto;
            }
        } catch (Exception e) {
            logger.error(MessageConstant.REG_FAIL, e);
            new BizException(MessageConstant.REG_FAIL,e.getMessage());
        }
        return null;
    }

    @Override
    public void loginCodeBuild(Long proxyId, String account) {
        try {
            if (!StringUtils.isMobileNO(account)) {
                return ;
            }
            String code = AliyunMnsUtil.randomSixCode();
            String redisKey = MessageFormat.format(LOGIN_MOBILE_CODE, account, proxyId);
            sendSMSCode(account, redisKey, code);
        } catch (Exception e) {
            logger.error(MessageConstant.FIND_USER_FAIL, e);
            new BizException(MessageConstant.FIND_USER_FAIL,e.getMessage());
        }
    }

    @Override
    public UserDTO loginCodeBuildVerification(String ip, Long proxyId, String account, String vcode) {
        try {
            if (StringUtils.isBlank(vcode)) {
                return null;
            }
            if (!StringUtils.isMobileNO(account)) {
                return null;
            }
            String key = MessageFormat.format(LOGIN_MOBILE_CODE, account, proxyId);
            String o = (String) redisTemplate.opsForValue().get(key);
            if (o == null) {
                return null;
            }
            if (!o.equals(vcode)) {
                return null;
            }
            ClientUserInfo userInfo = findByPhone(proxyId, account);
            if (userInfo == null) {
                return null;
            }
            ClientUserExtendInfo clientUserExtendInfo = clientUserExtendInfoService.findByUserCode(userInfo.getId().intValue());
            if (clientUserExtendInfo != null) {
                clientUserExtendInfo.setLastLoginIp(ip);
                clientUserExtendInfoService.save(clientUserExtendInfo);
            }
            redisTemplate.delete(key);
            UserDTO dto = new UserDTO();
            BeanCoper.copyProperties(dto, userInfo);
           return dto;
        } catch (Exception e) {
            logger.error(MessageConstant.FIND_USER_FAIL, e);
            new BizException(MessageConstant.FIND_USER_FAIL,e.getMessage());
        }
        return null;
    }

    @Override
    public UserDTO login(String ip, Long proxyId, String account, String passwrd) {
        try {
            if (StringUtils.isBlank(passwrd)) {
                return null;
            }
            if (!StringUtils.isMobileNO(account)) {
                return null;
            }

            ClientUserInfo userInfo = findByPhone(proxyId, account);
            if (userInfo == null) {
                return null;
            }

            passwrd = MD5.MD5Str(passwrd, passKey);
            if (!passwrd.equals(userInfo.getPasswd())) {
                return null;
            }

            ClientUserExtendInfo clientUserExtendInfo = clientUserExtendInfoService.findByUserCode(userInfo.getId().intValue());
            if (clientUserExtendInfo == null) {
               throw new BizException("find.clientUserExtendInfo.error","获取用户信息失败");
            }
            ClientUserExtendInfo userExtendInfo = new ClientUserExtendInfo();
            userExtendInfo.setLastLoginIp(ip);
            userExtendInfo.setId(clientUserExtendInfo.getId());
            clientUserExtendInfoService.save(userExtendInfo);
            String login_pin_key = MessageFormat.format(LOGIN_PIN, userInfo.getPin());
            UserDTO dto = (UserDTO) redisTemplate.opsForValue().get(login_pin_key);
            if (dto != null) {
                String login_pin_token = MessageFormat.format(LOGIN_PIN_TOKEN,userInfo.getPin(), dto.getToken());
                redisTemplate.delete(login_pin_token);
                dto.setToken(StringUtils.getUUID());
                redisTemplate.opsForValue().set(login_pin_key, dto, 7, TimeUnit.DAYS);
                redisTemplate.opsForValue().set(login_pin_token,dto.getToken(),7, TimeUnit.DAYS);
            }
            else{
                dto = new UserDTO();
                BeanCoper.copyProperties(dto, userInfo);
                dto.setToken(StringUtils.getUUID());
                redisTemplate.opsForValue().set(login_pin_key, dto, 7, TimeUnit.DAYS);
                String login_pin_token = MessageFormat.format(LOGIN_PIN_TOKEN,userInfo.getPin(), dto.getToken());
                redisTemplate.opsForValue().set(login_pin_token,dto.getToken(),7, TimeUnit.DAYS);
            }
            return dto;
        } catch (Exception e) {
            logger.error(MessageConstant.FIND_USER_FAIL, e);
            new BizException(MessageConstant.FIND_USER_FAIL,e.getMessage());
        }
        return null;
    }

    @Override
    public void initPass(Long proxyId, String pin, String passwd) {
        try {
            if (StringUtils.isBlank(pin)) {
                return ;
            }
            if (StringUtils.isBlank(passwd)) {
                return ;
            }
            ClientUserInfo userInfo = findByPin(proxyId,pin);
            if (userInfo == null) {
                return ;
            }
            userInfo.setPasswd(MD5.MD5Str(passwd, passKey));
            save(userInfo);
        } catch (Exception e) {
            logger.error(MessageConstant.SEND_CODE_FAIL, e);
            new BizException(MessageConstant.SEND_CODE_FAIL,e.getMessage());
        }
    }

    @Override
    public void changeMobileBuildMsg(Long proxyId, String pin, String mobile) {
        try {
            if (StringUtils.isBlank(pin)) {
                return ;
            }
            if (!StringUtils.isMobileNO(mobile)) {
                return ;
            }
            ClientUserInfo userInfo = findByPin(proxyId,pin);
            if (userInfo == null) {
                return ;
            }
            String code = AliyunMnsUtil.randomSixCode();
            String redisKey = MessageFormat.format(MOBILE_USER_CHANGE, mobile);
            sendSMSCode(mobile, redisKey, code);
        } catch (Exception e) {
            logger.error(MessageConstant.SEND_CODE_FAIL, e);
            new BizException(MessageConstant.SEND_CODE_FAIL,e.getMessage());
        }
    }

    @Override
    public void changeMobile(Long proxyId, String pin, String mobile) {
        try {
            if (StringUtils.isBlank(pin)) {
                return ;
            }
            if (!StringUtils.isMobileNO(mobile)) {
                return ;
            }
            ClientUserInfo userInfo = findByPin(proxyId,pin);
            if (userInfo == null) {
                return ;
            }
            String code = AliyunMnsUtil.randomSixCode();
            String redisKey = MessageFormat.format(MOBILE_USER_BIND, mobile);
            sendSMSCode(mobile, redisKey, code);

        } catch (Exception e) {
            logger.error(MessageConstant.SEND_CODE_FAIL, e);
            new BizException(MessageConstant.SEND_CODE_FAIL,e.getMessage());
        }
    }

    @Override
    public void bindMobile(Long proxyId, String pin, String mobile) {
        try {
            if (!StringUtils.isMobileNO(mobile)) {
                return ;
            }
            ClientUserInfo userInfo = findByPin(proxyId,pin);
            if (userInfo == null) {
                return ;
            }
            String key = MessageFormat.format(MOBILE_USER_BIND, mobile);
            String o = (String) redisTemplate.opsForValue().get(key);
            if (o == null) {
                return ;
            }
            userInfo.setPhone(mobile);
            save(userInfo);
        } catch (Exception e) {
            logger.error(MessageConstant.BIND_MOBILE_FAIL, e);
            new BizException(MessageConstant.BIND_MOBILE_FAIL,e.getMessage());
        }
    }

    @Override
    public void bindMobile(Long proxyId, String pin, String mobile, String msg) {
        try {
            if (StringUtils.isBlank(pin) || StringUtils.isBlank(msg)) {
                return ;
            }
            if (!StringUtils.isMobileNO(mobile)) {
                return ;
            }
            ClientUserInfo userInfo = findByPin(proxyId,pin);
            if (userInfo == null) {
                return ;
            }

            String key = MessageFormat.format(MOBILE_USER_BIND, mobile);
            String o = (String) redisTemplate.opsForValue().get(key);
            if (o == null) {
                return ;
            }

            if (!o.equals(msg)) {
                return ;
            }
            userInfo.setPhone(mobile);
            save(userInfo);
        } catch (Exception e) {
            logger.error(MessageConstant.BIND_MOBILE_FAIL, e);
            new BizException(MessageConstant.BIND_MOBILE_FAIL,e.getMessage());
        }
    }

    @Override
    public void changeMobile(Long proxyId, String pin, String mobile, String msg) {
        try {
            if (StringUtils.isBlank(pin) || StringUtils.isBlank(msg)) {
                return ;
            }
            if (!StringUtils.isMobileNO(mobile)) {
                return ;
            }
            ClientUserInfo userInfo = findByPin(proxyId,pin);
            if (userInfo == null) {
                return ;
            }

            String key = MessageFormat.format(MOBILE_USER_CHANGE, mobile);
            String o = (String) redisTemplate.opsForValue().get(key);
            if (o == null) {
                return ;
            }

            if (!o.equals(msg)) {
                return ;
            }

            userInfo.setPhone(mobile);
            save(userInfo);
        } catch (Exception e) {
            logger.error(MessageConstant.CHANGE_MOBILE_FAIL, e);
            new BizException(MessageConstant.CHANGE_MOBILE_FAIL,e.getMessage());
        }
    }

    @Override
    public void changePass(Long proxyId, String pin, String oldPass, String newPass) {
        try {
            if (StringUtils.isBlank(pin)) {
                return ;
            }
            ClientUserInfo userInfo = findByPin(proxyId,pin);
            if (userInfo == null) {
                return ;
            }

            oldPass = MD5.MD5Str(oldPass, passKey);
            if (!oldPass.equals(userInfo.getPasswd())) {
                return ;
            }

            userInfo.setPasswd(MD5.MD5Str(newPass, passKey));
            save(userInfo);
        } catch (Exception e) {
            logger.error(MessageConstant.CHANGE_PASS_FAIL, e);
            new BizException(MessageConstant.CHANGE_PASS_FAIL,e.getMessage());
        }

    }

    @Override
    public void changePassByMobile(Long proxyId, String pin, String mobile, String msg, String password) {
        try {
            if (StringUtils.isBlank(pin)) {
                return ;
            }

            if (!StringUtils.isMobileNO(mobile)) {
                return ;
            }
            ClientUserInfo userInfo =findByPin(proxyId,pin);
            if (userInfo == null) {
                return ;
            }
            if (!mobile.equals(userInfo.getPhone())) {
                return ;
            }
            String key = MessageFormat.format(PASS_USER_CHANGE_BY_MOBILE, mobile);
            String o = (String) redisTemplate.opsForValue().get(key);
            if (o == null) {
                return ;
            }
            if (!o.equals(msg)) {
                return ;
            }
            password = MD5.MD5Str(password, passKey);
            if (!password.equals(userInfo.getPasswd())) {
                return ;
            }
            userInfo.setPasswd(password);
            save(userInfo);
        } catch (Exception e) {
            logger.error(MessageConstant.CHANGE_PASS_FAIL, e);
            new BizException(MessageConstant.CHANGE_PASS_FAIL,e.getMessage());
        }
    }

    @Override
    public void changePassByMobileBuildMsg(Long proxyId, String pin, String mobile) {
        try {
            if (StringUtils.isBlank(pin)) {
                return ;
            }
            if (!StringUtils.isMobileNO(mobile)) {
                return ;
            }
            ClientUserInfo userInfo = findByPin(proxyId,pin);
            if (userInfo == null) {
                return ;
            }
            if (!mobile.equals(userInfo.getPhone())) {
                return ;
            }
            String code = AliyunMnsUtil.randomSixCode();
            String redisKey = MessageFormat.format(PASS_USER_CHANGE_BY_MOBILE, mobile);
            sendSMSCode(mobile, redisKey, code);
        } catch (Exception e) {
            logger.error(MessageConstant.SEND_CODE_FAIL, e);
            new BizException(MessageConstant.SEND_CODE_FAIL,e.getMessage());
        }
    }

    @Override
    public void forgetPass(Long proxyId, String pin) {
        try {
            if (StringUtils.isBlank(pin)) {
                return ;
            }
            ClientUserInfo userInfo = findByPin(proxyId,pin);
            if (userInfo == null) {
                return ;
            }
            String code = AliyunMnsUtil.randomSixCode();
            String mobile = userInfo.getPhone();
            String redisKey = MessageFormat.format(FORGET_PASS, pin);
            sendSMSCode(mobile, redisKey, code);
        } catch (Exception e) {
            logger.error(MessageConstant.SEND_CODE_FAIL, e);
            new BizException(MessageConstant.SEND_CODE_FAIL,e.getMessage());
        }
    }

    @Override
    public UserDTO forgetPassCodeVerification(Long proxyId, String pin, String code, String pass) {
        try {
            if (StringUtils.isBlank(pin)) {
                return null;
            }
            ClientUserInfo userInfo =findByPin(proxyId,pin);
            if (userInfo == null) {
                return null;
            }
            String key = MessageFormat.format(FORGET_PASS, pin);
            String o = (String) redisTemplate.opsForValue().get(key);
            if (o == null) {
                return null;
            }
            if (!o.equals(code)) {
                return null;
            }
            pass = MD5.MD5Str(pass, passKey);
            userInfo.setPasswd(pass);
            save(userInfo);
            UserDTO dto = new UserDTO();
            BeanCoper.copyProperties(dto,userInfo);
            return dto;
        } catch (Exception e) {
            logger.error(MessageConstant.SEND_CODE_FAIL, e);
            new BizException(MessageConstant.SEND_CODE_FAIL,e.getMessage());

        }
        return null;
    }

    @Override
    public void changeNickName(Long proxyId, String pin, String nickName) {
        try {
            if (StringUtils.isBlank(pin) || StringUtils.isBlank(nickName)) {
                return ;
            }
            ClientUserInfo userInfo = findByPin(proxyId,pin);
            if (userInfo == null) {
                return ;
            }
            userInfo.setNickName(nickName);
            save(userInfo);
        } catch (Exception e) {
            logger.error(MessageConstant.CHANGE_NICK_FAIL, e);
            new BizException(MessageConstant.CHANGE_NICK_FAIL,e.getMessage());
        }
    }

    @Override
    public void changeSex(Long proxyId, String pin, Integer sexType) {
        try {
            if (StringUtils.isBlank(pin) || (sexType != SexEnum.MALE.getValue() && sexType != SexEnum.FEMALE.getValue())) {
                return ;
            }

            ClientUserInfo userInfo = findByPin(proxyId,pin);
            if (userInfo == null) {
                return ;
            }
            userInfo.setSexType(sexType);
            save(userInfo);
        } catch (Exception e) {
            logger.error(MessageConstant.CHANGE_SEX_FAIL, e);
            new BizException(MessageConstant.CHANGE_SEX_FAIL,e.getMessage());
        }
    }

    @Override
    public void changeBirthday(Long proxyId, String pin, String date) {
        try {
            if (StringUtils.isBlank(pin) || StringUtils.isBlank(date)) {
                return ;
            }

            ClientUserInfo userInfo = findByPin(proxyId, pin);
            if (userInfo == null) {
                return ;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = sdf.parse(date);
            userInfo.setBirthDay(d);
            save(userInfo);
        } catch (Exception e) {
            logger.error(MessageConstant.CHANGE_BIRTHDAY_FAIL, e);
            new BizException(MessageConstant.CHANGE_BIRTHDAY_FAIL,e.getMessage());
        }
    }

    @Override
    public void saveUserExtendInfo(Long proxyId, UserExtendDTO userExtendDTO) {
        try {
            ClientUserExtendInfo entity = new ClientUserExtendInfo();
            BeanCoper.copyProperties(entity, userExtendDTO);
            clientUserExtendInfoService.save(entity);
        } catch (Exception e) {
            logger.error(MessageConstant.SAVE_USER_EXTEND_INFO_FAIL, e);
            new BizException(MessageConstant.SAVE_USER_EXTEND_INFO_FAIL,e.getMessage());
        }
    }

    @Override
    public void loginOut(String token) {
        try {
            String login_pin_token = MessageFormat.format(LOGIN_PIN, token);
            UserDTO dto = (UserDTO)redisTemplate.opsForValue().get(login_pin_token);
            if(dto == null){
                return;
            }
            String pin = dto.getPin();
            String login_pin_key = MessageFormat.format(LOGIN_PIN, pin);
            redisTemplate.delete(login_pin_key);
            redisTemplate.delete(login_pin_token);
        }catch (Exception e){
            logger.error(MessageConstant.SAVE_USER_EXTEND_INFO_FAIL, e);
            new BizException(MessageConstant.SAVE_USER_EXTEND_INFO_FAIL,e.getMessage());
        }

    }


    private void sendSMSCode(String mobile, String redisKey, String code) {
        String content = MessageFormat.format("您好!您的验证码:{0},有效时间3分钟，请及时验证!", code);
        SMSInfo info = new SMSInfo();
        info.setMobile(mobile);
        info.setContent(content);
        info.setSender("大三环");
        smsInfoService.insert(info);
        redisTemplate.opsForValue().set(redisKey, code, SysContant.MSGCODE_TIMEOUT, TimeUnit.SECONDS);
    }
}

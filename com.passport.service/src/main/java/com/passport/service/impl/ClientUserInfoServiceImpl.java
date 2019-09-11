package com.passport.service.impl;

import com.common.exception.ApplicationException;
import com.common.exception.BizException;
import com.common.mongo.AbstractMongoService;
import com.common.security.DesEncrypter;
import com.common.security.MD5;
import com.common.util.BeanCoper;
import com.common.util.StringUtils;
import com.common.util.model.SexEnum;
import com.common.util.model.YesOrNoEnum;
import com.passport.domain.ClientUserInfo;
import com.passport.domain.SMSInfo;
import com.passport.domain.module.UserStatusEnum;
import com.passport.rpc.dto.UserDTO;
import com.passport.service.ClientUserInfoService;
import com.passport.service.ProxyInfoService;
import com.passport.service.SMSInfoService;
import com.passport.service.constant.ChangeType;
import com.passport.service.constant.MessageConstant;
import com.passport.service.constant.SysContant;
import com.passport.service.util.AliyunMnsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class ClientUserInfoServiceImpl extends AbstractMongoService<ClientUserInfo> implements ClientUserInfoService {
    private static Logger logger = LoggerFactory.getLogger(ClientUserInfoServiceImpl.class);

    private final String CLIENT_USER_CACHE = "passport.cache.{0}";
    private final String PASS_USER_REG = "passport.userrpc.reg.account.{0}.proxyid.{1}";
    private final String LOGIN_MOBILE_CODE = "passport.userrpc.login.account.{0}.proxyid.{1}";
    private final String LOGIN_PIN = "passport.login.{0}.{1}";
    private final String LOGIN_TOKEN = "passport.login.token.{0}";
    private final String MOBILE_USER_CHANGE = "passport.userrpc.change.mobile.{0}";
    private final String MOBILE_USER_BIND = "passport.userrpc.bind.mobile.{0}";
    private final String PASS_USER_CHANGE_BY_MOBILE = "passport.userrpc.changepass.mobile.{0}";
    private final String FORGET_PASS = "passport.userrpc.forgetpass.pin.{0}";
    private final String LAST_SEND_SMS_TIME = "passport.send.sms.last.time.{0}";
    private final String FOTGETPASS_SEND_SMS = "passport.send.sms.forget.pass.count.{0}";
    private final String REGISTER_SEND_SMS = "passport.send.sms.register.count.{0}";
    /**
     * 用户session   时间时长
     */
    public final static int USER_SESSION_TIME = 60 * 24 * 30;
    @Value("${app.passKey}")
    private String passKey;

    @Value("${app.token.encode.key}")
    private String appTokenEncodeKey;


    @Value("${app.sms.interval.min}")
    private Long intervalMin;

    @Value("${app.sms.limit.register}")
    private Integer regSmsLimit;

    @Value("${app.sms.limit.forgetPass}")
    private Integer forgetPassSmsLimit;

    @Resource
    private SMSInfoService smsInfoService;
    @Resource
    private ProxyInfoService proxyInfoService;
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
        if (query == null) {
            return null;
        }
        return query;
    }

    public ClientUserInfo findByPin(Long proxyId, String pin) {
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

    public ClientUserInfo findByPhone(Long proxyId, String phone) {
        ClientUserInfo clientUserInfo = new ClientUserInfo();
        clientUserInfo.setProxyId(proxyId);
        clientUserInfo.setPhone(phone);
        return this.findByOne(clientUserInfo);
    }

    @Override
    public Page<ClientUserInfo> queryByNick(Long proxyId, String nickName, Pageable pageable) {
        ClientUserInfo clientUserInfo = new ClientUserInfo();
        clientUserInfo.setProxyId(proxyId);
        clientUserInfo.setNickName(nickName);
        return this.queryByPage(clientUserInfo, pageable);
    }

    public void changePass(Long proxyId, String pin, String pwd) {
        ClientUserInfo byPin = findByPin(proxyId, pin);
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
    public void regist(Long proxyId, String account, String refPin) {
        if (StringUtils.isBlank(account)) {
            throw new BizException("账号不能为空");
        }
        if (!StringUtils.isMobileNO(account) && !StringUtils.isEmail(account)) {
            throw new BizException("账号格式不正确");
        }
        ClientUserInfo entity = findByPhone(proxyId, account);
        if (entity != null && entity.getStatus().equals(UserStatusEnum.Normal.getValue())) {
            throw new BizException("该账号已存在");
        }
        //检查验证码限制
        String key = MessageFormat.format(REGISTER_SEND_SMS, account);
        int count = 0;
        if (redisTemplate.hasKey(key)) {
            long expire = redisTemplate.getExpire(key);
            long now = System.currentTimeMillis() / 1000;
            if (now > expire) {
                redisTemplate.delete(key);
            } else {
                count = Integer.parseInt(redisTemplate.opsForValue().get(key).toString());
                if (count >= regSmsLimit) {
                    throw new BizException("今日获取注册验证码超过限制");
                }
            }
        }
        String code = AliyunMnsUtil.randomSixCode();
        String redisKey = MessageFormat.format(PASS_USER_REG, account, proxyId);
        sendSMSCode(account, redisKey, code, proxyInfoService.findBySeqId(proxyId).getName());

        Date expireDate = com.passport.service.util.DateUtil.getTomorrowZeroTime();
        redisTemplate.opsForValue().set(key, count + 1);
        redisTemplate.expireAt(key, expireDate);
    }

    @Override
    public UserDTO registVerification(Long proxyId, String account, String vcode, String pass) {
        String key = MessageFormat.format(PASS_USER_REG, account, proxyId);
        if (!redisTemplate.hasKey(key)) {
            throw new BizException("验证码过期");
        }
        String o = (String) redisTemplate.opsForValue().get(key);
        if (!o.equalsIgnoreCase(vcode)) {
            throw new BizException("验证码错误");
        }
        return regist(proxyId, "", pass, null);
    }

    @Override
    public void loginCodeBuild(Long proxyId, String account) {
        try {
            if (!StringUtils.isMobileNO(account) && !StringUtils.isEmail(account)) {
                throw new BizException("账号格式不正确");
            }

            String code = AliyunMnsUtil.randomSixCode();
            String redisKey = MessageFormat.format(LOGIN_MOBILE_CODE, account, proxyId);
            sendSMSCode(account, redisKey, code, proxyInfoService.findBySeqId(proxyId).getName());
        } catch (Exception e) {
            logger.error(MessageConstant.FIND_USER_FAIL, e);
            new BizException(MessageConstant.FIND_USER_FAIL, e.getMessage());
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
            if (!redisTemplate.hasKey(key)) {
                throw new BizException("验证码过期");
            }
            String o = (String) redisTemplate.opsForValue().get(key);
            if (!o.equals(vcode)) {
                throw new BizException("验证码错误");
            }
            ClientUserInfo userInfo = findByPhone(proxyId, account);
            if (userInfo == null) {
                return null;
            }

            String login_pin_key = MessageFormat.format(LOGIN_PIN, proxyId, userInfo.getPin());
            Object o1 = redisTemplate.opsForValue().get(login_pin_key);
            String newToken = StringUtils.getUUID();
            UserDTO dto = null;
            if (o1 != null) {
                String oldTokenKey = o1.toString();
                redisTemplate.delete(oldTokenKey);
            }
            dto = new UserDTO();
            BeanCoper.copyProperties(dto, userInfo);

            String newTokenKey = MessageFormat.format(LOGIN_TOKEN, newToken);
            redisTemplate.opsForValue().set(login_pin_key, newToken, 7, TimeUnit.DAYS);
            redisTemplate.opsForValue().set(newTokenKey, dto, 7, TimeUnit.DAYS);
            //删除验证码
            redisTemplate.delete(key);
            return dto;
        } catch (Exception e) {
            logger.error(MessageConstant.FIND_USER_FAIL, e);
            new BizException(MessageConstant.FIND_USER_FAIL, e.getMessage());
        }
        return null;
    }

    @Override
    public UserDTO login(String ip, Long proxyId, String account, String passwrd) {

        if (StringUtils.isBlank(passwrd)) {
            throw new BizException("参数不能为空");
        }

        if (account.indexOf("_") < 0 && !StringUtils.isMobileNO(account)) {
            throw new BizException("手机号格式不正确");
        }

        ClientUserInfo userInfo = findByPhone(proxyId, account);
        if (userInfo == null || UserStatusEnum.Disable.getValue() == userInfo.getStatus().intValue()) {
            throw new BizException("无法找到该用户,请注册");
        }

        passwrd = MD5.MD5Str(passwrd, passKey);
        if (!passwrd.equals(userInfo.getPasswd())) {
            throw new BizException("密码错误");
        }
        UserDTO userDTO = buildToken(proxyId, userInfo);
        return userDTO;
    }

    private UserDTO buildToken(Long proxyId, ClientUserInfo userInfo) {
        String login_pin_key = MessageFormat.format(LOGIN_PIN, proxyId, userInfo.getPin());
        Object o = redisTemplate.opsForValue().get(login_pin_key);
        if (o != null) {
            String oldTokenKey = o.toString();
            oldTokenKey = MessageFormat.format(LOGIN_TOKEN, oldTokenKey);
            redisTemplate.delete(oldTokenKey);
            redisTemplate.delete(login_pin_key);
        }
        String newToken = StringUtils.getUUID();
        UserDTO dto = new UserDTO();
        dto.setPin(userInfo.getPin());
        BeanCoper.copyProperties(dto, userInfo);
        dto.setToken(newToken);
        String newTokenKey = MessageFormat.format(LOGIN_TOKEN, newToken);
        redisTemplate.opsForValue().set(login_pin_key, newToken, 7, TimeUnit.DAYS);
        redisTemplate.opsForValue().set(newTokenKey, dto, 7, TimeUnit.DAYS);

        String token = dto.getProxyId() + ":" + dto.getPin() + ":" + dto.getToken();
        token = DesEncrypter.cryptString(token, appTokenEncodeKey);
        dto.setToken(token);
        return dto;
    }


    @Override
    public void initPass(Long proxyId, String pin, String passwd) {
        try {
            if (StringUtils.isBlank(pin) || StringUtils.isBlank(passwd)) {
                throw new BizException("参数不能为空");
            }
            ClientUserInfo userInfo = findByPin(proxyId, pin);
            if (userInfo == null) {
                throw new BizException("无法找到该用户");
            }
            userInfo.setPasswd(MD5.MD5Str(passwd, passKey));
            save(userInfo);
        } catch (Exception e) {
            logger.error(MessageConstant.SEND_CODE_FAIL, e);
            new BizException(MessageConstant.SEND_CODE_FAIL, e.getMessage());
        }
    }

    @Override
    public void delById(String id) {
        throw new ApplicationException("删除失败，方法已禁用");
    }

    @Override
    public void changeMobileBuildMsg(Long proxyId, String pin, String mobile) {
        try {
            if (StringUtils.isBlank(pin)) {
                throw new BizException("参数不能为空");
            }
            if (!StringUtils.isMobileNO(mobile)) {
                throw new BizException("手机号格式不正确");
            }
            ClientUserInfo userInfo = findByPin(proxyId, pin);
            if (userInfo == null) {
                throw new BizException("无法找到该用户");
            }
            String code = AliyunMnsUtil.randomSixCode();
            String redisKey = MessageFormat.format(MOBILE_USER_CHANGE, mobile);
            sendSMSCode(mobile, redisKey, code, proxyInfoService.findBySeqId(proxyId).getName());
        } catch (Exception e) {
            logger.error(MessageConstant.SEND_CODE_FAIL, e);
            new BizException(MessageConstant.SEND_CODE_FAIL, e.getMessage());
        }
    }

    @Override
    public void changeMobile(Long proxyId, String pin, String mobile) {
        try {
            if (StringUtils.isBlank(pin)) {
                throw new BizException("参数不能为空");
            }
            if (!StringUtils.isMobileNO(mobile)) {
                throw new BizException("手机号格式不正确");
            }
            ClientUserInfo userInfo = findByPin(proxyId, pin);
            if (userInfo == null) {
                throw new BizException("无法找到该用户");
            }
            String code = AliyunMnsUtil.randomSixCode();
            String redisKey = MessageFormat.format(MOBILE_USER_CHANGE, mobile);
            sendSMSCode(mobile, redisKey, code, proxyInfoService.findBySeqId(proxyId).getName());

        } catch (Exception e) {
            logger.error(MessageConstant.SEND_CODE_FAIL, e);
            new BizException(MessageConstant.SEND_CODE_FAIL, e.getMessage());
        }
    }

    @Override
    public void bindMobile(Long proxyId, String pin, String mobile) {
        try {
            if (!StringUtils.isMobileNO(mobile)) {
                throw new BizException("手机号格式不正确");
            }
            ClientUserInfo userInfo = findByPin(proxyId, pin);
            if (userInfo == null) {
                throw new BizException("无法找到该用户");
            }

            String code = AliyunMnsUtil.randomSixCode();
            String redisKey = MessageFormat.format(MOBILE_USER_BIND, mobile);

            sendSMSCode(mobile, redisKey, code, proxyInfoService.findBySeqId(proxyId).getName());
        } catch (Exception e) {
            logger.error(MessageConstant.BIND_MOBILE_FAIL, e);
            new BizException(MessageConstant.BIND_MOBILE_FAIL, e.getMessage());
        }
    }

    @Override
    public void bindMobile(Long proxyId, String pin, String mobile, String msg) {
        try {
            if (StringUtils.isBlank(pin) || StringUtils.isBlank(msg)) {
                throw new BizException("参数不能为空");
            }
            if (!StringUtils.isMobileNO(mobile)) {
                throw new BizException("手机号格式不正确");
            }
            ClientUserInfo userInfo = findByPin(proxyId, pin);
            if (userInfo == null) {
                throw new BizException("无法找到该用户");
            }

            String key = MessageFormat.format(MOBILE_USER_BIND, mobile);
            if (!redisTemplate.hasKey(key)) {
                throw new BizException("验证码过期");
            }
            String o = (String) redisTemplate.opsForValue().get(key);
            if (!o.equals(msg)) {
                throw new BizException("验证码错误");
            }
            userInfo.setPhone(mobile);
            save(userInfo);
        } catch (Exception e) {
            logger.error(MessageConstant.BIND_MOBILE_FAIL, e);
            new BizException(MessageConstant.BIND_MOBILE_FAIL, e.getMessage());
        }
    }

    @Override
    public void changeMobile(Long proxyId, String pin, String mobile, String msg) {
        try {
            if (StringUtils.isBlank(pin) || StringUtils.isBlank(msg)) {
                throw new BizException("参数不能为空");
            }
            if (!StringUtils.isMobileNO(mobile)) {
                throw new BizException("手机号格式不正确");
            }
            ClientUserInfo userInfo = findByPin(proxyId, pin);
            if (userInfo == null)
                throw new BizException("无法找到该用户");

            String key = MessageFormat.format(MOBILE_USER_CHANGE, mobile);
            if (!redisTemplate.hasKey(key)) {
                throw new BizException("验证码过期");
            }
            String o = (String) redisTemplate.opsForValue().get(key);
            if (!o.equals(msg)) {
                throw new BizException("验证码错误");
            }

            userInfo.setPhone(mobile);
            save(userInfo);
        } catch (Exception e) {
            logger.error(MessageConstant.CHANGE_MOBILE_FAIL, e);
            new BizException(MessageConstant.CHANGE_MOBILE_FAIL, e.getMessage());
        }
    }

    @Override
    public void changePass(Long proxyId, String pin, String oldPass, String newPass) {
        try {
            if (StringUtils.isBlank(pin)) {
                throw new BizException("参数不能为空");
            }
            ClientUserInfo userInfo = findByPin(proxyId, pin);
            if (userInfo == null) {
                throw new BizException("无法找到该用户");
            }

            oldPass = MD5.MD5Str(oldPass, passKey);
            if (!oldPass.equals(userInfo.getPasswd())) {
                throw new BizException("密码错误");
            }

            userInfo.setPasswd(MD5.MD5Str(newPass, passKey));
            save(userInfo);
        } catch (Exception e) {
            logger.error(MessageConstant.CHANGE_PASS_FAIL, e);
            new BizException(MessageConstant.CHANGE_PASS_FAIL, e.getMessage());
        }

    }

    @Override
    public void changePassByMobile(Long proxyId, String pin, String mobile, String msg, String password) {
        try {
            if (StringUtils.isBlank(pin)) {
                throw new BizException("参数不能为空");
            }

            if (!StringUtils.isMobileNO(mobile)) {
                throw new BizException("手机号格式不正确");
            }
            ClientUserInfo userInfo = findByPin(proxyId, pin);
            if (userInfo == null) {
                throw new BizException("无法找到该用户");
            }
            if (!mobile.equals(userInfo.getPhone())) {
                throw new BizException("手机号错误");
            }
            String key = MessageFormat.format(PASS_USER_CHANGE_BY_MOBILE, mobile);
            if (!redisTemplate.hasKey(key)) {
                throw new BizException("验证码过期");
            }
            String o = (String) redisTemplate.opsForValue().get(key);
            if (!o.equals(msg)) {
                throw new BizException("验证码错误");
            }
            password = MD5.MD5Str(password, passKey);
            if (!password.equals(userInfo.getPasswd())) {
                throw new BizException("密码错误");
            }
            userInfo.setPasswd(password);
            save(userInfo);
        } catch (Exception e) {
            logger.error(MessageConstant.CHANGE_PASS_FAIL, e);
            new BizException(MessageConstant.CHANGE_PASS_FAIL, e.getMessage());
        }
    }

    @Override
    public void changePassByMobileBuildMsg(Long proxyId, String pin, String mobile) {
        try {
            if (StringUtils.isBlank(pin)) {
                throw new BizException("参数不能为空");
            }

            if (!StringUtils.isMobileNO(mobile)) {
                throw new BizException("手机号格式不正确");
            }
            ClientUserInfo userInfo = findByPin(proxyId, pin);
            if (userInfo == null) {
                throw new BizException("无法找到该用户");
            }
            if (!mobile.equals(userInfo.getPhone())) {
                throw new BizException("手机号错误");
            }
            String code = AliyunMnsUtil.randomSixCode();
            String redisKey = MessageFormat.format(PASS_USER_CHANGE_BY_MOBILE, mobile);
            sendSMSCode(mobile, redisKey, code, proxyInfoService.findBySeqId(proxyId).getName());
        } catch (Exception e) {
            logger.error(MessageConstant.SEND_CODE_FAIL, e);
            new BizException(MessageConstant.SEND_CODE_FAIL, e.getMessage());
        }
    }

    @Override
    public void forgetPass(Long proxyId, String phone) {
        try {
            if (StringUtils.isBlank(phone)) {
                throw new BizException("参数不能为空");
            }
            ClientUserInfo userInfo = findByPhone(proxyId, phone);
            if (userInfo == null) {
                throw new BizException("无法找到该用户");
            }
            String code = AliyunMnsUtil.randomSixCode();
            String mobile = userInfo.getPhone();
            String redisKey = MessageFormat.format(FORGET_PASS, phone);
            sendSMSCode(mobile, redisKey, code, proxyInfoService.findBySeqId(proxyId).getName());
        } catch (Exception e) {
            logger.error(MessageConstant.SEND_CODE_FAIL, e);
            new BizException(MessageConstant.SEND_CODE_FAIL, e.getMessage());
        }
    }

    @Override
    public UserDTO forgetPassCodeVerification(Long proxyId, String phone, String code, String pass) {
        try {
            if (StringUtils.isBlank(phone)) {
                throw new BizException("参数不能为空");
            }
            ClientUserInfo userInfo = findByPhone(proxyId, phone);
            if (userInfo == null) {
                throw new BizException("无法找到该用户");
            }
            String key = MessageFormat.format(FORGET_PASS, phone);
            if (!redisTemplate.hasKey(key)) {
                throw new BizException("验证码过期");
            }
            String o = (String) redisTemplate.opsForValue().get(key);
            if (!o.equals(code)) {
                throw new BizException("验证码错误");
            }
            pass = MD5.MD5Str(pass, passKey);
            userInfo.setPasswd(pass);
            save(userInfo);
            UserDTO dto = new UserDTO();
            BeanCoper.copyProperties(dto, userInfo);
            return dto;
        } catch (Exception e) {
            logger.error(MessageConstant.SEND_CODE_FAIL, e);
            new BizException(MessageConstant.SEND_CODE_FAIL, e.getMessage());

        }
        return null;
    }

    @Override
    public void changeNickName(Long proxyId, String pin, String nickName) {
        try {
            if (StringUtils.isBlank(pin) || StringUtils.isBlank(nickName)) {
                throw new BizException("参数不能为空");
            }
            ClientUserInfo userInfo = findByPin(proxyId, pin);
            if (userInfo == null) {
                throw new BizException("无法找到该用户");
            }
            userInfo.setNickName(nickName);
            save(userInfo);
        } catch (Exception e) {
            logger.error(MessageConstant.CHANGE_NICK_FAIL, e);
            new BizException(MessageConstant.CHANGE_NICK_FAIL, e.getMessage());
        }
    }

    @Override
    public void changeSex(Long proxyId, String pin, Integer sexType) {
        try {
            if (StringUtils.isBlank(pin) || (sexType != SexEnum.MALE.getValue() && sexType != SexEnum.FEMALE.getValue())) {
                throw new BizException("参数错误");
            }

            ClientUserInfo userInfo = findByPin(proxyId, pin);
            if (userInfo == null) {
                throw new BizException("无法找到该用户");
            }
            userInfo.setSexType(sexType);
            save(userInfo);
        } catch (Exception e) {
            logger.error(MessageConstant.CHANGE_SEX_FAIL, e);
            new BizException(MessageConstant.CHANGE_SEX_FAIL, e.getMessage());
        }
    }

    @Override
    public void changeBirthday(Long proxyId, String pin, String date) {
        try {
            if (StringUtils.isBlank(pin) || StringUtils.isBlank(date)) {
                throw new BizException("参数不能为空");
            }

            ClientUserInfo userInfo = findByPin(proxyId, pin);
            if (userInfo == null) {
                throw new BizException("无法找到该用户");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = sdf.parse(date);
            userInfo.setBirthDay(d);
            save(userInfo);
        } catch (Exception e) {
            logger.error(MessageConstant.CHANGE_BIRTHDAY_FAIL, e);
            new BizException(MessageConstant.CHANGE_BIRTHDAY_FAIL, e.getMessage());
        }
    }

    @Override
    public void loginOut(Long proxyId, String pin, String token) {
        try {
            String login_pin_key = MessageFormat.format(LOGIN_PIN, proxyId, pin);
            UserDTO dto = (UserDTO) redisTemplate.opsForValue().get(login_pin_key);
            if (dto == null) {
                return;
            }
            String login_pin_token = MessageFormat.format(LOGIN_TOKEN, token);
            redisTemplate.delete(login_pin_key);
            redisTemplate.delete(login_pin_token);
        } catch (Exception e) {
            logger.error(MessageConstant.SAVE_USER_EXTEND_INFO_FAIL, e);
            new BizException(MessageConstant.SAVE_USER_EXTEND_INFO_FAIL, e.getMessage());
        }

    }


    private void sendSMSCode(String mobile, String redisKey, String code, String sign) {
        String key = MessageFormat.format(LAST_SEND_SMS_TIME, mobile);

        long now = System.currentTimeMillis();
        if (redisTemplate.hasKey(key)) {
            long lastTime = Long.parseLong(redisTemplate.opsForValue().get(key).toString());
            if ((now - lastTime) < intervalMin) {
                throw new BizException("发送短信过于频繁");
            }
        } else {
            redisTemplate.opsForValue().set(key, now, 5, TimeUnit.MINUTES);
        }
        String content = MessageFormat.format("您好!您的验证码:{0},有效时间3分钟，请及时验证!", code);
        SMSInfo info = new SMSInfo();
        info.setSign(sign);
        info.setMobile(mobile);
        info.setContent(content);
        info.setSender("passport");
        smsInfoService.insert(info);
        redisTemplate.opsForValue().set(redisKey, code, SysContant.MSGCODE_TIMEOUT, TimeUnit.SECONDS);
    }

    @Override
    public Page<ClientUserInfo> QueryRegisterUsers(Integer pageNum, Date startTime, Date endTime) {
        try {
            ClientUserInfo info = new ClientUserInfo();
            info.setStartCreateTime(startTime);
            info.setEndCreateTime(endTime);
            return this.queryByPage(info, new PageRequest(pageNum, 30));
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    @Override
    public UserDTO regist(Long proxyId, String refId, String pass, String phone) {

        if (StringUtils.isNotBlank(phone) && !StringUtils.isMobileNO(phone)) {
            logger.error("电话号码错误 " + phone);
            throw new BizException("phone.error", "电话号码错误 " + phone);
        }
        if (StringUtils.isBlank(refId)) {
            throw new BizException("refId.error", "参考账户不能为空 ");
        }
        ClientUserInfo entity = findByPhone(proxyId, phone);
        if (entity != null && entity.getStatus().equals(UserStatusEnum.Normal.getValue())) {
            logger.error("该账号已经注册过了 " + phone);
            throw new BizException("该账号已经注册过");
        }
        if (entity == null) {
            entity = new ClientUserInfo();
        }
        entity.setProxyId(proxyId);
        entity.setRefId(refId);
        entity.setPhone(phone);
        entity.setSexType(SexEnum.MALE.getValue());
        entity.setPasswd(MD5.MD5Str(pass, passKey));
        entity.setStatus(UserStatusEnum.Normal.getValue());
        super.save(entity);
        return buildToken(proxyId, entity);
    }

    @Override
    public UserDTO registGuest(Long proxyId, String deviceId, String refPin) {
        ClientUserInfo entity = new ClientUserInfo();
        entity.setDeviceUid(deviceId);
        entity.setProxyId(proxyId);
        entity.setStatus(YesOrNoEnum.YES.getValue());
        entity.setRefPin(refPin);
        insert(entity);
        return buildToken(proxyId, entity);
    }

    @Override
    public UserDTO loginByDeviceUid(Long proxyId, String deviceId) {
        ClientUserInfo entity = new ClientUserInfo();
        entity.setDeviceUid(deviceId);
        entity.setProxyId(proxyId);
        entity=findByOne(entity);
        return buildToken(proxyId, entity);
    }

    @Override
    public void insert(ClientUserInfo entity) {
        if (entity.getSexType() == null) {
            entity.setSexType(SexEnum.MALE.getValue());
        }
        if (entity.getStatus() == null) {
            entity.setStatus(UserStatusEnum.Normal.getValue());
        }
        super.insert(entity);
        ClientUserInfo upEntity = new ClientUserInfo();
        upEntity.setId(entity.getId());
        upEntity.setPin(entity.getSeqId().toString());
        entity.setPin(entity.getSeqId().toString());
        up(upEntity);
    }

    @Override
    public void proxyChangeUserInfo(Long proxyId, String userAccount, ChangeType type, String value) {
        try {
            if (proxyId == null || StringUtils.isBlank(userAccount) || StringUtils.isBlank(value)) {
                throw new BizException("参数不能为空");
            }

            ClientUserInfo user = findByPhone(proxyId, userAccount);
            if (user == null) {
                throw new BizException("无法找到该用户");
            }

            switch (type) {
                case PASS:
                    user.setPasswd(MD5.MD5Str(value, passKey));
                    break;
                case BIRTH:
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    user.setBirthDay(sdf.parse(value));
                    break;
                case EMAIL:
                    if (!StringUtils.isEmail(value)) {
                        return;
                    }
                    user.setEmail(value);
                    break;
                case PHONE:
                    if (!StringUtils.isMobileNO(value)) {
                        return;
                    }
                    user.setPhone(value);
                    break;
            }
            save(user);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    @Override
    public Page<ClientUserInfo> proxyGetUsers(Long proxyId, Integer pageNum) {
        try {
            if (proxyId == null) {
                throw new BizException("参数不能为空");
            }
            if (pageNum == null || pageNum < 0) {
                pageNum = 0;
            }

            ClientUserInfo info = new ClientUserInfo();
            info.setProxyId(proxyId);
            return queryByPage(info, new PageRequest(pageNum, 30));
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    @Override
    public Page<ClientUserInfo> queryByRegTime(Long proxyId, Date startRegTime, Date endRegTime, Pageable pageable) {
        try {
            ClientUserInfo info = new ClientUserInfo();
            info.setStartCreateTime(startRegTime);
            info.setEndCreateTime(endRegTime);
            return queryByPage(info, pageable);
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    @Override
    public Long queryCountByRegTime(Long proxyId, Date startRegTime, Date endRegTime) {
        try {
            ClientUserInfo info = new ClientUserInfo();
            info.setProxyId(proxyId);
            info.setStartCreateTime(startRegTime);
            info.setEndCreateTime(endRegTime);
            return queryCount(info);
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }


}

package com.passport.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.common.exception.BizException;
import com.common.mongo.AbstractMongoService;
import com.common.security.DesEncrypter;
import com.common.security.MD5;
import com.common.util.BeanCoper;
import com.common.util.DateUtil;
import com.common.util.Result;
import com.common.util.StringUtils;
import com.common.util.model.SexEnum;
import com.common.util.model.YesOrNoEnum;
import com.passport.domain.ClientUserExtendInfo;
import com.passport.domain.ClientUserInfo;
import com.passport.domain.SMSInfo;
import com.passport.domain.LimitInfo;
import com.passport.domain.module.UserStatusEnum;
import com.passport.rpc.dto.LimitType;
import com.passport.rpc.dto.ProxyDto;
import com.passport.service.*;
import com.passport.rpc.dto.UserDTO;
import com.passport.rpc.dto.UserExtendDTO;
import com.passport.service.constant.ChangeType;
import com.passport.service.constant.HttpStatusCode;
import com.passport.service.constant.MessageConstant;
import com.passport.service.constant.SysContant;
import com.passport.service.util.AliyunMnsUtil;
import com.passport.service.util.OldPackageMapUtil;
import com.passport.service.util.Tool;
import com.platform.rpc.RecommendRPCService;
import org.apache.log4j.Logger;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ClientUserInfoServiceImpl extends AbstractMongoService<ClientUserInfo> implements ClientUserInfoService {
    private static Logger logger = Logger.getLogger(ClientUserInfoServiceImpl.class);

    private final String CLIENT_USER_CACHE = "passport.cache.{0}";
    private final String PASS_USER_REG = "passport.userrpc.reg.account.{0}.proxyid.{1}";
    private final String LOGIN_MOBILE_CODE = "passport.userrpc.login.account.{0}.proxyid.{1}";
    private final String LOGIN_PIN = "passport.login.{0}";
    private final String LOGIN_TOKEN = "passport.login.token.{0}";
    private final String MOBILE_USER_CHANGE = "passport.userrpc.change.mobile.{0}";
    private final String MOBILE_USER_BIND = "passport.userrpc.bind.mobile.{0}";
    private final String PASS_USER_CHANGE_BY_MOBILE = "passport.userrpc.changepass.mobile.{0}";
    private final String FORGET_PASS = "passport.userrpc.forgetpass.pin.{0}";
    /**
     * 用户session 时间时长
     */
    public final static int USER_SESSION_TIME = 60 * 24 * 30;
    @Value("${app.passKey}")
    private String passKey;

    @Value("${app.token.encode.key}")
    private String appTokenEncodeKey;
    @Value("${reg.recommend.url}")
    private String recommendUrl;
    @Value("${server.tomcat.basedir}")
    private String imgTempDir;
    @Resource
    private SMSInfoService smsInfoService;
    @Resource
    private LogLoginService logLoginService;
    @Resource
    private ClientUserExtendInfoService clientUserExtendInfoService;
    @Resource
    private LimitInfoService limitInfoService;
    @Reference(check = false)
    private RecommendRPCService recommendRPCService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private Tool tool;

    protected Class<ClientUserInfo> getEntityClass() {
        return ClientUserInfo.class;
    }



    public ClientUserInfo login(Long proxyId, String loginName, String passwd, String ip) {
        //查询此ip是否被限制登陆
        if(isLoginLimit(ip,null,null)){
            throw new BizException("login.error", "当前ip被限制登陆");
        }

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
        //查询此用户是否被限制登陆
        if(isLoginLimit(null,proxyId,query.getPin())){
            throw new BizException("login.error", "当前用户被限制登陆");
        }
        logLoginService.addLoginLog(query.getPin(), proxyId, query.getCreateTime(), ip,query.getId());
        return null;
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

    public ClientUserInfo findByUserCode(Long proxyId, Integer userCode) {
        ClientUserInfo clientUserInfo = new ClientUserInfo();
        clientUserInfo.setProxyId(proxyId);
        clientUserInfo.setId(userCode.longValue());
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
            throw new BizException("账号不能为空");
        }
        if (!StringUtils.isMobileNO(account) && !StringUtils.isEmail(account)) {
            throw new BizException("账号格式不正确");
        }
        ClientUserInfo entity = findByPhone(proxyId, account);
        if (entity != null) {
            throw new BizException("该账号已存在");
        }
        String code = AliyunMnsUtil.randomSixCode();
        String redisKey = MessageFormat.format(PASS_USER_REG, account, proxyId);
        sendSMSCode(account, redisKey, code);
    }

    @Override
    public UserDTO registVerification(ProxyDto proxydto, String account, String vcode, String pass, String ip) {
        String key = MessageFormat.format(PASS_USER_REG, account, proxydto.getId());
        String o = (String) redisTemplate.opsForValue().get(key);
        if (o.equalsIgnoreCase(vcode)) {
            return regist(proxydto, null,account, pass, account, null, null, SexEnum.MALE, null, ip, null, null, null, null, null);
        }
        return null;
    }

    @Override
    public void loginCodeBuild(Long proxyId, String account) {
        try {
            if (!StringUtils.isMobileNO(account) && !StringUtils.isEmail(account)) {
                throw new BizException("账号格式不正确");
            }
            String code = AliyunMnsUtil.randomSixCode();
            String redisKey = MessageFormat.format(LOGIN_MOBILE_CODE, account, proxyId);
            sendSMSCode(account, redisKey, code);
        } catch (Exception e) {
            logger.error(MessageConstant.FIND_USER_FAIL, e);
            new BizException(MessageConstant.FIND_USER_FAIL, e.getMessage());
        }
    }

    @Override
    public UserDTO loginCodeBuildVerification(String ip, Long proxyId, String account, String vcode) {
        try {
            //查询此ip是否被限制登陆
            if(isLoginLimit(ip,null,null)){
                throw new BizException("login.error", "当前ip被限制登陆");
            }

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

            //查询此用户是否被限制登陆
            if(isLoginLimit(null,proxyId,userInfo.getPin())){
                throw new BizException("login.error", "当前用户被限制登陆");
            }

            String login_pin_key = MessageFormat.format(LOGIN_PIN, userInfo.getPin());
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
            redisTemplate.opsForValue().set(login_pin_key, newTokenKey, 7, TimeUnit.DAYS);
            redisTemplate.opsForValue().set(newTokenKey, dto, 7, TimeUnit.DAYS);
            //删除验证码
            redisTemplate.delete(key);
            logLoginService.addLoginLog(dto.getPin(), proxyId, userInfo.getCreateTime(), ip,userInfo.getId());
            return dto;
        } catch (Exception e) {
            logger.error(MessageConstant.FIND_USER_FAIL, e);
            new BizException(MessageConstant.FIND_USER_FAIL, e.getMessage());
        }
        return null;
    }

    @Override
    public UserDTO login(String ip, Long proxyId, String account, String passwrd) {
        //查询此ip是否被限制登陆
        if(isLoginLimit(ip,null,null)){
            throw new BizException("login.error", "当前ip被限制登陆");
        }


        if (StringUtils.isBlank(passwrd)) {
            throw new BizException("参数不能为空");
        }
        if (!StringUtils.isMobileNO(account)) {
            throw new BizException("手机号格式不正确");
        }

        ClientUserInfo userInfo = findByPhone(proxyId, account);
        if (userInfo == null) {
            throw new BizException("无法找到该用户");
        }

        //查询此用户是否被限制登陆
        if(isLoginLimit(null,proxyId,userInfo.getPin())){
            throw new BizException("login.error", "当前用户被限制登陆");
        }


        passwrd = MD5.MD5Str(passwrd, passKey);
        if (!passwrd.equals(userInfo.getPasswd())) {
            throw new BizException("密码错误");
        }

        ClientUserExtendInfo clientUserExtendInfo = clientUserExtendInfoService.findByUserCode(userInfo.getId().intValue());
        if (clientUserExtendInfo == null) {
            clientUserExtendInfo = new ClientUserExtendInfo();
            clientUserExtendInfo.setId(clientUserExtendInfo.getId());
            clientUserExtendInfoService.save(clientUserExtendInfo);
        }
        String login_pin_key = MessageFormat.format(LOGIN_PIN, userInfo.getPin());
        Object o = redisTemplate.opsForValue().get(login_pin_key);

        if (o != null) {
            String oldTokenKey = o.toString();
            oldTokenKey=MessageFormat.format(LOGIN_TOKEN, oldTokenKey);
            redisTemplate.delete(oldTokenKey);
            redisTemplate.delete(login_pin_key);
        }
        String newToken = StringUtils.getUUID();
        UserDTO dto  = new UserDTO();
        BeanCoper.copyProperties(dto, userInfo);
        dto.setToken(newToken);
        String newTokenKey = MessageFormat.format(LOGIN_TOKEN, newToken);
        redisTemplate.opsForValue().set(login_pin_key, newToken, 7, TimeUnit.DAYS);
        redisTemplate.opsForValue().set(newTokenKey, dto, 7, TimeUnit.DAYS);
        logLoginService.addLoginLog(dto.getPin(), proxyId, userInfo.getCreateTime(), ip,userInfo.getId());
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
            sendSMSCode(mobile, redisKey, code);
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
            String redisKey = MessageFormat.format(MOBILE_USER_BIND, mobile);
            sendSMSCode(mobile, redisKey, code);

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
            sendSMSCode(mobile, redisKey, code);
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
            String o = (String) redisTemplate.opsForValue().get(key);
            if (o == null) {
                throw new BizException("验证码错误");
            }

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
            String o = (String) redisTemplate.opsForValue().get(key);
            if (o == null)
                throw new BizException("验证码错误");

            if (!o.equals(msg))
                throw new BizException("验证码错误");

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
            String o = (String) redisTemplate.opsForValue().get(key);
            if (o == null) {
                throw new BizException("验证码错误");
            }
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
            sendSMSCode(mobile, redisKey, code);
        } catch (Exception e) {
            logger.error(MessageConstant.SEND_CODE_FAIL, e);
            new BizException(MessageConstant.SEND_CODE_FAIL, e.getMessage());
        }
    }

    @Override
    public void forgetPass(Long proxyId, String pin) {
        try {
            if (StringUtils.isBlank(pin)) {
                throw new BizException("参数不能为空");
            }
            ClientUserInfo userInfo = findByPin(proxyId, pin);
            if (userInfo == null) {
                throw new BizException("无法找到该用户");
            }
            String code = AliyunMnsUtil.randomSixCode();
            String mobile = userInfo.getPhone();
            String redisKey = MessageFormat.format(FORGET_PASS, pin);
            sendSMSCode(mobile, redisKey, code);
        } catch (Exception e) {
            logger.error(MessageConstant.SEND_CODE_FAIL, e);
            new BizException(MessageConstant.SEND_CODE_FAIL, e.getMessage());
        }
    }

    @Override
    public UserDTO forgetPassCodeVerification(Long proxyId, String pin, String code, String pass) {
        try {
            if (StringUtils.isBlank(pin)) {
                throw new BizException("参数不能为空");
            }
            ClientUserInfo userInfo = findByPin(proxyId, pin);
            if (userInfo == null) {
                throw new BizException("无法找到该用户");
            }
            String key = MessageFormat.format(FORGET_PASS, pin);
            String o = (String) redisTemplate.opsForValue().get(key);
            if (o == null) {
                throw new BizException("验证码错误");
            }
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
    public void saveUserExtendInfo(Long proxyId, UserExtendDTO userExtendDTO) {
        try {
            ClientUserExtendInfo entity = new ClientUserExtendInfo();
            BeanCoper.copyProperties(entity, userExtendDTO);
            clientUserExtendInfoService.save(entity);
        } catch (Exception e) {
            logger.error(MessageConstant.SAVE_USER_EXTEND_INFO_FAIL, e);
            new BizException(MessageConstant.SAVE_USER_EXTEND_INFO_FAIL, e.getMessage());
        }
    }

    @Override
    public void loginOut(String pin, String token) {
        try {
            String login_pin_key = MessageFormat.format(LOGIN_PIN, pin);
            UserDTO dto = (UserDTO) redisTemplate.opsForValue().get(login_pin_key);
            if (dto == null) {
                return;
            }
            String login_pin_token = MessageFormat.format(LOGIN_PIN, pin, token);
            redisTemplate.delete(login_pin_key);
            redisTemplate.delete(login_pin_token);
        } catch (Exception e) {
            logger.error(MessageConstant.SAVE_USER_EXTEND_INFO_FAIL, e);
            new BizException(MessageConstant.SAVE_USER_EXTEND_INFO_FAIL, e.getMessage());
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
    public UserDTO regist(ProxyDto proxydto,String recommendId, String refId, String pass, String phone, String nick, String email,
                          SexEnum sexEnum, String birth, String ip, String headUrl, String wechat, String idCard,
                          String realName, Long qq) {
        //查询此ip是否被限制注册
        if(isRegisterLimit(ip)){
            throw new BizException("regist.error", "当前ip被限制注册");
        }

        if (proxydto == null) {
            throw new BizException("proxyId.error", "代理商id错误");
        }
        if (StringUtils.isNotBlank(phone) && !StringUtils.isMobileNO(phone)) {
            throw new BizException("phone.error", "电话号码错误");
        }
        if (StringUtils.isBlank(refId)) {
            throw new BizException("refId.error", "参考账户不能为空");
        }
        if (StringUtils.isNotBlank(email) && !StringUtils.isMobileNO(email)) {
            throw new BizException("email.error", "邮件地址错误");
        }
        Date date = null;
        if (!StringUtils.isBlank(birth)) {
            date = DateUtil.parseDate(birth);
        }
        Long proxyId = proxydto.getId();

        ClientUserInfo entity = findByPhone(proxyId,phone);
        if(entity != null){
            throw new BizException("该账号已经注册过了");
        }
        if(StringUtils.isBlank(nick)){
            nick = "玩家" + phone.substring(7);
        }

        entity = new ClientUserInfo();
        entity.setProxyId(proxyId);
        entity.setRefId(refId);
        String pin = StringUtils.getUUID();
        entity.setPin(pin);
        entity.setPhone(phone);
        entity.setNickName(nick);
        entity.setSexType(sexEnum.getValue());
        entity.setStatus(UserStatusEnum.Normal.getValue());
        entity.setPasswd(MD5.MD5Str(pass, passKey));
        entity.setBirthDay(date);
        entity.setEmail(email);
        entity.setHeadUrl(headUrl);
        entity.setWechat(wechat);
        entity.setIdCard(idCard);
        entity.setRealName(realName);
        entity.setQq(qq);
        entity.setRegisterIp(ip);

        String fileName = proxydto.getId() + "_" + phone;
        try {
            String domain = "http://passport." + proxydto.getDomain();
            String url = MessageFormat.format(recommendUrl,domain,pin);
            Result<String> res = tool.generateQRCode(url, fileName, imgTempDir);
            if(res.getSuccess()){
                entity.setQrName(res.getModule());
            }
        }catch (Exception e){
            logger.error("生成二维码异常");
        }

        save(entity);

        ClientUserExtendInfo clientUserExtendInfo = new ClientUserExtendInfo();
        clientUserExtendInfo.setUserCode(entity.getId().intValue());
        clientUserExtendInfo.setRobot(YesOrNoEnum.NO.getValue());
        clientUserExtendInfoService.save(clientUserExtendInfo);

        UserDTO dto = new UserDTO();
        BeanCoper.copyProperties(dto, entity);
        recommendRPCService.init(pin,recommendId,proxyId);
        limitInfoService.addIpRegisterNum(ip);
        return dto;
    }

    @Override
    public Long insert(ClientUserInfo entity) {
        if(entity.getSexType() == null){
            entity.setSexType(SexEnum.MALE.getValue());
        }
        if(entity.getStatus() == null){
            entity.setStatus(UserStatusEnum.Normal.getValue());
        }
        if(entity.getQq() == null){
            entity.setQq(0L);
        }
        return super.insert(entity);
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
            info.setStartCreateTime(startRegTime);
            info.setEndCreateTime(endRegTime);
            return queryCount(info);
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }


    /************************************以下是兼容以前的接口**********************************************/


    @Override
    public Map<String,Object> oldUpdatePwd(Long proxyId, String pin, String pwd, String newPwd) {
        try{
            if (proxyId == null || StringUtils.isBlank(pin)) {
                return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"参数不能为空");
            }
            ClientUserInfo user = findByPin(proxyId,pin);
            if(user == null){
                return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"未找到该用户");
            }
            pwd = MD5.MD5Str(pwd, passKey);
            if(!pwd.equals(user.getPasswd())){
                return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"密码校验失败");
            }
            int length = newPwd.trim().length();
            if(length < 6 || length > 16){
                return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"密码长度在6-16位");
            }
            this.changePass(proxyId,user.getPin(),newPwd);
        }catch (Exception e){
            logger.error("", e);
            return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"修改密码异常");
        }
        return OldPackageMapUtil.toSuccessMap(HttpStatusCode.CODE_ACCEPTED,"修改秘密码成功");
    }

    @Override
    public Map<String, Object> oldForgetPass(Long proxyId, String account, String code,String pwd) {
        try{
            if (proxyId == null) {
                return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"参数不能为空");
            }
            if (!StringUtils.isMobileNO(account)) {
                return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"手机号格式不正确");
            }
            ClientUserInfo user = findByPhone(proxyId, account);
            if(user == null){
                return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"未找到该用户");
            }

            /*String key = MessageFormat.format(PASS_USER_CHANGE_BY_MOBILE, account);
            String o = (String) redisTemplate.opsForValue().get(key);
            if (o == null) {
                return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"验证码错误");
            }
            if (!o.equals(code)) {
                return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"验证码错误");
            }*/

            pwd = MD5.MD5Str(pwd, passKey);
            user.setPasswd(pwd);
            save(user);
        }catch (Exception e){
            logger.error("",e);
            return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"修改密码异常");
        }
        return OldPackageMapUtil.toSuccessMap(HttpStatusCode.CODE_ACCEPTED,"修改秘密码成功");
    }

    @Override
    public Map<String, Object> oldFindByUserCode(Long proxyId, String pin) {
        try{
            if (proxyId == null || StringUtils.isBlank(pin)) {
                return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"参数不能为空");
            }
            ClientUserInfo info = findByPin(proxyId, pin);
            if(info == null){
                return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"未找到该用户");
            }
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("userId", info.getId());
            dataMap.put("accessName", info.getPhone());
            dataMap.put("gender", info.getSexType());
            dataMap.put("nick", info.getNickName());
            dataMap.put("headUrl", info.getHeadUrl());
            ClientUserExtendInfo extend = clientUserExtendInfoService.findByUserCode(info.getId().intValue());
            if(extend != null){
                dataMap.put("sign", extend.getSign());
            }
            return OldPackageMapUtil.toMap(HttpStatusCode.CODE_OK,HttpStatusCode.MSG_OK,true,dataMap);
        }catch (Exception e){
            logger.error("",e);
            return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"获取用户信息异常");
        }
    }

    @Override
    public Map<String, Object> oldFindByAccount(Long proxyId, String account) {
        try{
            if (proxyId == null || account == null) {
                return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"参数不能为空");
            }
            if(!StringUtils.isMobileNO(account)){
                return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"账号格式错误");
            }
            ClientUserInfo info = findByPhone(proxyId,account);
            if(info == null){
                return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"未找到该用户");
            }
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("userId", info.getId());
            dataMap.put("accessName", info.getPhone());
            dataMap.put("gender", info.getSexType());
            dataMap.put("nick", info.getNickName());
            dataMap.put("headUrl", info.getHeadUrl());
            ClientUserExtendInfo extend = clientUserExtendInfoService.findByUserCode(info.getId().intValue());
            if(extend != null){
                dataMap.put("sign", extend.getSign());
            }
            return OldPackageMapUtil.toMap(HttpStatusCode.CODE_OK,HttpStatusCode.MSG_OK,true,dataMap);
        }catch (Exception e){
            logger.error("",e);
            return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"获取用户信息异常");
        }
    }

    @Override
    public Map<String, Object> oldEditUserInfo(Long proxyId, String pin, String nick, String qq, String wechat, String sex, String sign) {
        Map<String, Object> dataMap = null;
        try{
            if (proxyId == null || StringUtils.isBlank(pin)) {
                return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"参数不能为空");
            }

            ClientUserInfo userInfo = findByPin(proxyId,pin);
            if(userInfo == null){
                return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"未找到该用户");
            }

            dataMap = new HashMap<>();
            if(StringUtils.isNotBlank(nick)){
                userInfo.setNickName(nick);
            }
            if(StringUtils.isNotBlank(qq) && tool.isNo(qq)){
                userInfo.setQq(Long.parseLong(qq));
            }
            if(StringUtils.isNotBlank(wechat)){
                userInfo.setWechat(wechat);
            }
            if(StringUtils.isBlank(sex) && tool.isNo(sex)){
                int sexInt = Integer.parseInt(sex);
                if(SexEnum.MALE.getValue() == sexInt || SexEnum.FEMALE.getValue() == sexInt){
                    userInfo.setSexType(sexInt);
                }
            }
            if(StringUtils.isNotBlank(sign)){
                ClientUserExtendInfo extend = clientUserExtendInfoService.findByUserCode(userInfo.getId().intValue());
                if(extend != null){
                    extend.setSign(sign);
                    dataMap.put("sign",extend.getSign());
                    clientUserExtendInfoService.save(extend);
                }
            }
            save(userInfo);
            dataMap.put("nick",userInfo.getNickName());
            dataMap.put("qq",userInfo.getQq());
            dataMap.put("wechat",userInfo.getWechat());
            dataMap.put("gender",userInfo.getSexType());
        }catch (Exception e){
            logger.error("",e);
            return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"编辑用户信息异常");
        }
        return OldPackageMapUtil.toSuccessMap(HttpStatusCode.CODE_OK,HttpStatusCode.MSG_OK,dataMap);
    }

    @Override
    public Map<String, Object> OldCertification(Long proxyId, String pin, String realName, String idCard) {
        try{
            if (proxyId == null || StringUtils.isBlank(pin) || StringUtils.isBlank(realName) || StringUtils.isBlank(idCard)) {
                return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"参数不能为空");
            }

            ClientUserInfo userInfo = findByPin(proxyId,pin);
            if(userInfo == null){
                return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"未找到该用户");
            }
            userInfo.setRealName(realName);
            userInfo.setIdCard(idCard);
            save(userInfo);
        }catch (Exception e){
            logger.error("",e);
            return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"实名认证异常");
        }
        return OldPackageMapUtil.toSuccessMap(HttpStatusCode.CODE_OK,HttpStatusCode.MSG_OK);
    }

    @Override
    public Long save(ClientUserInfo entity) {
        String userKey = MessageFormat.format(CLIENT_USER_CACHE, entity.getPin());
        ClientUserInfo cacheUser = (ClientUserInfo) redisTemplate.opsForValue().get(userKey);
        if (cacheUser != null) {
            redisTemplate.delete(userKey);
        }
        return super.save(entity);
    }

    @Override
    public Map<String, Object> oldRegist(ProxyDto proxydto, String account, String vcode, String pass, String ip,String recommendId) {
        try {
            String head = String.valueOf(1 + (int) (Math.random() * 20));
            int length = pass.trim().length();
            if(length < 6 || length > 16){
                return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"密码长度在6-16位");
            }
            /*String key = MessageFormat.format(PASS_USER_REG, account, proxydto.getId());
            String o = (String) redisTemplate.opsForValue().get(key);
            if (!o.equalsIgnoreCase(vcode)){
                return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"验证码错误");
            }*/
            UserDTO userDTO = regist(proxydto, recommendId,account, pass, account, null, null, SexEnum.MALE, null, ip, head, null, null, null, null);
            if(userDTO == null){
                return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"注册失败");
            }
        }catch (Exception e){
            logger.error("",e);
            return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,e.getMessage());
        }
        return OldPackageMapUtil.toSuccessMap(HttpStatusCode.MSG_OK,"注册成功");
    }

    @Override
    public Map<String, Object> oldRegistBuildCode(Long proxyId, String phone) {
        try{
            String redisKey = MessageFormat.format(PASS_USER_REG, phone,proxyId);
            return oldBuildCode(proxyId,phone,redisKey);
        }catch (Exception e){
            logger.error("",e);
            return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,e.getMessage());
        }
    }

    @Override
    public Map<String, Object> oldForgetPassBuildCode(Long proxyId, String phone) {
        try{
            String redisKey = MessageFormat.format(PASS_USER_CHANGE_BY_MOBILE, phone);
            return oldBuildCode(proxyId,phone,redisKey);
        }catch (Exception e){
            logger.error("",e);
            return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,e.getMessage());
        }
    }

    private Map<String, Object> oldBuildCode(Long proxyId, String phone,String redisKey){
        if (!StringUtils.isMobileNO(phone)) {
            return OldPackageMapUtil.toFailMap(HttpStatusCode.CODE_BAD_REQUEST,"手机号格式不正确");
        }
        String code = AliyunMnsUtil.randomSixCode();
        sendSMSCode(phone, redisKey, code);
        return OldPackageMapUtil.toSuccessMap(HttpStatusCode.CODE_OK,"发送验证码成功");
    }

    /**
     * 是否被登陆限制
     * @param ip
     * @param proxyId
     * @param pin
     * @return
     */
    private Boolean isLoginLimit(String ip,Long proxyId,String pin){
        LimitInfo userLimitInfo = null;
        if(!StringUtils.isBlank(ip)){
            userLimitInfo = limitInfoService.findByIp(ip);
        }else if(proxyId != null && !StringUtils.isBlank(pin)){
            userLimitInfo = limitInfoService.findByPin(proxyId, pin);
        }else{
            return false;
        }

        if(userLimitInfo == null){
            return false;
        }

        if(userLimitInfo.getLimitType() != LimitType.Login.getValue()){
            return false;
        }

        //比较时间
        Long now = new Date().getTime();
        Long limitStartTime = userLimitInfo.getLimitStartTime().getTime();
        Long limitEndTime = userLimitInfo.getLimitEndTime().getTime();
        if(now < limitStartTime || now > limitEndTime){
            return false;
        }
        return true;
    }

    /**
     * ip是否被限制注册
     * @param ip
     * @return
     */
    private Boolean isRegisterLimit(String ip){
        if(StringUtils.isBlank(ip)){
            return false;
        }

        //获取被限制的总数量
        LimitInfo allNumInfo = limitInfoService.findAllLimitNum();
        Integer allNum = null;
        if(allNumInfo != null){
            allNum = allNumInfo.getAllNum();
        }

        //获取当前IP的限制信息
        LimitInfo limitInfo = limitInfoService.findByIp(ip);
        if(limitInfo == null){
            return false;
        }

        //获取当前ip已经注册的数量,比较是否超过限制
        Integer currenNum = limitInfo.getCurrentIpRegNum();
        if(allNum != null && currenNum != null && currenNum >= allNum){
            return true;
        }

        //如果没有超过，检查是否被系统限制注册
        Integer limitType = limitInfo.getLimitType();
        if(limitType == null || limitType != LimitType.Register.getValue()){
            return false;
        }

        //比较时间
        Long now = new Date().getTime();
        Date limitStartDate = limitInfo.getLimitStartTime();
        Date limitEndDate = limitInfo.getLimitEndTime();
        if(limitStartDate == null || limitEndDate == null){
            return false;
        }
        Long limitStartTime = limitStartDate.getTime();
        Long limitEndTime = limitEndDate.getTime();
        if(now < limitStartTime || now > limitEndTime){
            return false;
        }
        return true;
    }
}

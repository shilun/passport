package com.passport.service.rpc;

import com.common.security.MD5;
import com.common.util.BeanCoper;
import com.common.util.RPCResult;
import com.common.util.StringUtils;
import com.common.util.model.SexEnum;
import com.common.util.model.YesOrNoEnum;
import com.passport.domain.ClientUserExtendInfo;
import com.passport.domain.ClientUserInfo;
import com.passport.rpc.UserRPCService;
import com.passport.rpc.dto.UserDTO;
import com.passport.rpc.dto.UserExtendDTO;
import com.passport.service.ClientUserExtendInfoService;
import com.passport.service.ClientUserInfoService;
import com.passport.service.constant.CodeConstant;
import com.passport.service.constant.MessageConstant;
import com.passport.service.constant.SysContant;
import com.passport.service.util.AliMsgUtil;
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
@com.alibaba.dubbo.config.annotation.Service
public class UserRPCServiceImpl implements UserRPCService {

    private final Logger logger = Logger.getLogger(UserRPCServiceImpl.class);

    @Value("${app.passKey}")
    private String passKey;
    /**
     * 用户注册redis key
     */
    private final String PASS_USER_REG = "passport.userrpc.reg.account.{0}.proxyid.{1}";
    private final String PASS_USER_CHANGE_BY_MOBILE = "passport.userrpc.changepass.mobile.{0}";
    private final String MOBILE_USER_CHANGE = "passport.userrpc.change.mobile.{0}";
    private final String MOBILE_USER_BIND = "passport.userrpc.bind.mobile.{0}";
    private final String LOGIN_MOBILE_CODE = "passport.userrpc.login.account.{0}.proxyid.{1}";
    private final String FORGET_PASS = "passport.userrpc.forgetpass.pin.{0}";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Resource
    private ClientUserInfoService clientUserInfoService;
    @Resource
    private ClientUserExtendInfoService clientUserExtendInfoService;

    @Override
    public RPCResult<Boolean> regist(Long proxyId,String account) {
        RPCResult result = new RPCResult();
        if (StringUtils.isBlank(account)) {
            result.setCode(CodeConstant.PARAM_NULL);
            result.setMessage(MessageConstant.PARAM_NULL);
            result.setSuccess(false);
            return result;
        }
        if (!StringUtils.isMobileNO(account)) {
            result.setCode(CodeConstant.PARAM_FORMAT_ERROR);
            result.setMessage(MessageConstant.PARAM_FORMAT_ERROR);
            result.setSuccess(false);
            return result;
        }
        ClientUserInfo entity = clientUserInfoService.findByPhone(proxyId,account);
        if (entity != null) {
            result.setCode(CodeConstant.MOBILE_EXIST);
            result.setMessage(MessageConstant.MOBILE_EXIST);
            result.setSuccess(false);
            return result;
        }

        String code = AliMsgUtil.randomSixCode();
        if(AliMsgUtil.sendMsgSingle(account,code)){
            redisTemplate.opsForValue().set(MessageFormat.format(PASS_USER_REG,account,proxyId),code,SysContant.MSGCODE_TIMEOUT,TimeUnit.SECONDS);
            result.setSuccess(true);
        }else{
            result.setSuccess(false);
            result.setCode(CodeConstant.SEND_CODE_FAIL);
            result.setMessage(MessageConstant.SEND_CODE_FAIL);
        }
        return result;
    }

    @Override
    public RPCResult<UserDTO> registVerification(Long proxyId,String account, String vcode, String pass) {
        RPCResult<UserDTO> result = new RPCResult<>();
        try {
            String key = MessageFormat.format(PASS_USER_REG, account,proxyId);
            String o = (String) redisTemplate.opsForValue().get(key);
            if (o.equalsIgnoreCase(vcode)) {
                result.setSuccess(true);
                ClientUserInfo entity = new ClientUserInfo();
                entity.setProxyId(proxyId);
                entity.setPin(StringUtils.getUUID());
                entity.setPhone(account);
                entity.setNickName(account);
                entity.setSexType(SexEnum.MALE.getValue());
                entity.setStatus(YesOrNoEnum.YES.getValue());
                entity.setPasswd(MD5.MD5Str(pass, passKey));
                clientUserInfoService.save(entity);
                UserDTO dto = new UserDTO();
                BeanCoper.copyProperties(dto, entity);
                result.setData(dto);
                result.setSuccess(true);
                return result;
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode(CodeConstant.REG_FAIL);
            result.setMessage(MessageConstant.REG_FAIL);
            logger.error(MessageConstant.REG_FAIL, e);
        }
        return result;
    }

    @Override
    public RPCResult<UserDTO> findByPin(String pin) {
        RPCResult<UserDTO> result = new RPCResult<>();
        try {
            if (StringUtils.isBlank(pin)) {
                result.setCode(CodeConstant.PARAM_NULL);
                result.setMessage(MessageConstant.PARAM_NULL);
                result.setSuccess(false);
                return result;
            }

            ClientUserInfo userInfo = clientUserInfoService.findByPin(pin);
            if(userInfo == null){
                result.setCode(CodeConstant.USER_NULL);
                result.setMessage(MessageConstant.USER_NULL);
                result.setSuccess(false);
                return result;
            }

            UserDTO dto = new UserDTO();
            BeanCoper.copyProperties(dto, userInfo);
            result.setData(dto);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode(CodeConstant.FIND_USER_FAIL);
            result.setMessage(MessageConstant.FIND_USER_FAIL);
            logger.error(MessageConstant.FIND_USER_FAIL, e);
        }
        return result;
    }

    @Override
    public RPCResult<UserDTO> findByAccount(Long proxyId,String account) {
        RPCResult<UserDTO> result = new RPCResult<>();
        try {
            if (!StringUtils.isMobileNO(account)) {
                result.setCode(CodeConstant.PARAM_FORMAT_ERROR);
                result.setMessage(MessageConstant.PARAM_FORMAT_ERROR);
                result.setSuccess(false);
                return result;
            }
            ClientUserInfo userInfo = clientUserInfoService.findByPhone(proxyId,account);
            if(userInfo == null){
                result.setCode(CodeConstant.USER_NULL);
                result.setMessage(MessageConstant.USER_NULL);
                result.setSuccess(false);
                return result;
            }

            UserDTO dto = new UserDTO();
            BeanCoper.copyProperties(dto, userInfo);
            result.setData(dto);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode(CodeConstant.FIND_USER_FAIL);
            result.setMessage(MessageConstant.FIND_USER_FAIL);
            logger.error(MessageConstant.FIND_USER_FAIL, e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> loginCodeBuild(Long proxyId,String account) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            if (!StringUtils.isMobileNO(account)) {
                result.setCode(CodeConstant.PARAM_FORMAT_ERROR);
                result.setMessage(MessageConstant.PARAM_FORMAT_ERROR);
                result.setSuccess(false);
                return result;
            }

            String code = AliMsgUtil.randomSixCode();
            if(AliMsgUtil.sendMsgSingle(account,code)){
                redisTemplate.opsForValue().set(MessageFormat.format(LOGIN_MOBILE_CODE,account,proxyId),code,SysContant.MSGCODE_TIMEOUT,TimeUnit.SECONDS);
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
                result.setCode(CodeConstant.SEND_CODE_FAIL);
                result.setMessage(MessageConstant.SEND_CODE_FAIL);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode(CodeConstant.FIND_USER_FAIL);
            result.setMessage(MessageConstant.FIND_USER_FAIL);
            logger.error(MessageConstant.FIND_USER_FAIL, e);
        }
        return result;
    }

    @Override
    public RPCResult<UserDTO> loginCodeBuildVerification(String ip,Long proxyId,String account, String vcode) {
        RPCResult<UserDTO> result = new RPCResult<>();
        try {
            if (StringUtils.isBlank(vcode)) {
                result.setCode(CodeConstant.PARAM_NULL);
                result.setMessage(MessageConstant.PARAM_NULL);
                result.setSuccess(false);
                return result;
            }
            if (!StringUtils.isMobileNO(account)) {
                result.setCode(CodeConstant.PARAM_FORMAT_ERROR);
                result.setMessage(MessageConstant.PARAM_FORMAT_ERROR);
                result.setSuccess(false);
                return result;
            }

            String key = MessageFormat.format(LOGIN_MOBILE_CODE, account,proxyId);
            String o = (String) redisTemplate.opsForValue().get(key);
            if(o == null){
                result.setCode(CodeConstant.CODE_TIMEOUT);
                result.setMessage(MessageConstant.CODE_TIMEOUT);
                result.setSuccess(false);
                return result;
            }

            if(!o.equals(vcode)){
                result.setCode(CodeConstant.VERIFICATION_FAIL);
                result.setMessage(MessageConstant.VERIFICATION_FAIL);
                result.setSuccess(false);
                return result;
            }

            ClientUserInfo userInfo = clientUserInfoService.findByPhone(proxyId,account);
            if (userInfo == null) {
                result.setCode(CodeConstant.USER_NULL);
                result.setMessage(MessageConstant.USER_NULL);
                result.setSuccess(false);
                return result;
            }

            ClientUserExtendInfo clientUserExtendInfo = clientUserExtendInfoService.findByUserCode(userInfo.getId().intValue());
            if(clientUserExtendInfo != null){
                clientUserExtendInfo.setLastLoginIp(ip);
                clientUserExtendInfoService.save(clientUserExtendInfo);
            }

            redisTemplate.delete(key);
            UserDTO dto = new UserDTO();
            BeanCoper.copyProperties(dto, userInfo);
            result.setData(dto);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode(CodeConstant.FIND_USER_FAIL);
            result.setMessage(MessageConstant.FIND_USER_FAIL);
            logger.error(MessageConstant.FIND_USER_FAIL, e);
        }
        return result;
    }

    @Override
    public RPCResult<UserDTO> login(String ip,Long proxyId,String account, String passwrd) {
        RPCResult<UserDTO> result = new RPCResult<>();
        try {
            if (StringUtils.isBlank(passwrd)) {
                result.setCode(CodeConstant.PARAM_NULL);
                result.setMessage(MessageConstant.PARAM_NULL);
                result.setSuccess(false);
                return result;
            }
            if (!StringUtils.isMobileNO(account)) {
                result.setCode(CodeConstant.PARAM_FORMAT_ERROR);
                result.setMessage(MessageConstant.PARAM_FORMAT_ERROR);
                result.setSuccess(false);
                return result;
            }

            ClientUserInfo userInfo = clientUserInfoService.findByPhone(proxyId,account);
            if (userInfo == null) {
                result.setCode(CodeConstant.USER_NULL);
                result.setMessage(MessageConstant.USER_NULL);
                result.setSuccess(false);
                return result;
            }

            passwrd = MD5.MD5Str(passwrd,passKey);
            if(!passwrd.equals(userInfo.getPasswd())){
                result.setCode(CodeConstant.PASS_ERROR);
                result.setMessage(MessageConstant.PASS_ERROR);
                result.setSuccess(false);
                return result;
            }

            ClientUserExtendInfo clientUserExtendInfo = clientUserExtendInfoService.findByUserCode(userInfo.getId().intValue());
            if(clientUserExtendInfo != null){
                clientUserExtendInfo.setLastLoginIp(ip);
                clientUserExtendInfoService.save(clientUserExtendInfo);
            }

            UserDTO dto = new UserDTO();
            BeanCoper.copyProperties(dto, userInfo);
            result.setData(dto);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode(CodeConstant.FIND_USER_FAIL);
            result.setMessage(MessageConstant.FIND_USER_FAIL);
            logger.error(MessageConstant.FIND_USER_FAIL, e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> initPass(String pin, String passwd) {
        return null;
    }

    @Override
    public RPCResult<Boolean> changeMobileBuildMsg(String pin, String mobile) {
        RPCResult<Boolean> result = new RPCResult();
        try {
            if (StringUtils.isBlank(pin)) {
                result.setCode(CodeConstant.PARAM_NULL);
                result.setMessage(MessageConstant.PARAM_NULL);
                result.setSuccess(false);
                return result;
            }
            if (!StringUtils.isMobileNO(mobile)) {
                result.setCode(CodeConstant.PARAM_FORMAT_ERROR);
                result.setMessage(MessageConstant.PARAM_FORMAT_ERROR);
                result.setSuccess(false);
                return result;
            }
            ClientUserInfo userInfo = clientUserInfoService.findByPin(pin);
            if (userInfo == null) {
                result.setCode(CodeConstant.USER_NULL);
                result.setMessage(MessageConstant.USER_NULL);
                result.setSuccess(false);
                return result;
            }

            String code = AliMsgUtil.randomSixCode();
            boolean res = AliMsgUtil.sendMsgSingle(mobile, code);
            if(res){
                redisTemplate.opsForValue().set(MessageFormat.format(MOBILE_USER_CHANGE,mobile),code,SysContant.MSGCODE_TIMEOUT,TimeUnit.SECONDS);
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
                result.setCode(CodeConstant.SEND_CODE_FAIL);
                result.setMessage(MessageConstant.SEND_CODE_FAIL);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode(CodeConstant.SEND_CODE_FAIL);
            result.setMessage(MessageConstant.SEND_CODE_FAIL);
            logger.error(MessageConstant.SEND_CODE_FAIL,e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> bindMobileBuildMsg(String pin, String mobile) {
        RPCResult<Boolean> result = new RPCResult();
        try {
            if (StringUtils.isBlank(pin)) {
                result.setCode(CodeConstant.PARAM_NULL);
                result.setMessage(MessageConstant.PARAM_NULL);
                result.setSuccess(false);
                return result;
            }
            if (!StringUtils.isMobileNO(mobile)) {
                result.setCode(CodeConstant.PARAM_FORMAT_ERROR);
                result.setMessage(MessageConstant.PARAM_FORMAT_ERROR);
                result.setSuccess(false);
                return result;
            }
            ClientUserInfo userInfo = clientUserInfoService.findByPin(pin);
            if (userInfo == null) {
                result.setCode(CodeConstant.USER_NULL);
                result.setMessage(MessageConstant.USER_NULL);
                result.setSuccess(false);
                return result;
            }

            String code = AliMsgUtil.randomSixCode();
            boolean res = AliMsgUtil.sendMsgSingle(mobile, code);
            if(res){
                redisTemplate.opsForValue().set(MessageFormat.format(MOBILE_USER_BIND,mobile),code,SysContant.MSGCODE_TIMEOUT,TimeUnit.SECONDS);
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
                result.setCode(CodeConstant.SEND_CODE_FAIL);
                result.setMessage(MessageConstant.SEND_CODE_FAIL);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode(CodeConstant.SEND_CODE_FAIL);
            result.setMessage(MessageConstant.SEND_CODE_FAIL);
            logger.error(MessageConstant.SEND_CODE_FAIL,e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> bindMobile(String pin, String mobile, String msg) {
        RPCResult<Boolean> result = new RPCResult();
        try {
            if (StringUtils.isBlank(pin) || StringUtils.isBlank(msg)) {
                result.setCode(CodeConstant.PARAM_NULL);
                result.setMessage(MessageConstant.PARAM_NULL);
                result.setSuccess(false);
                return result;
            }
            if (!StringUtils.isMobileNO(mobile)) {
                result.setCode(CodeConstant.PARAM_FORMAT_ERROR);
                result.setMessage(MessageConstant.PARAM_FORMAT_ERROR);
                result.setSuccess(false);
                return result;
            }
            ClientUserInfo userInfo = clientUserInfoService.findByPin(pin);
            if (userInfo == null) {
                result.setCode(CodeConstant.USER_NULL);
                result.setMessage(MessageConstant.USER_NULL);
                result.setSuccess(false);
                return result;
            }

            String key = MessageFormat.format(MOBILE_USER_BIND, mobile);
            String o = (String) redisTemplate.opsForValue().get(key);
            if(o == null){
                result.setCode(CodeConstant.CODE_TIMEOUT);
                result.setMessage(MessageConstant.CODE_TIMEOUT);
                result.setSuccess(false);
                return result;
            }

            if(!o.equals(msg)){
                result.setCode(CodeConstant.VERIFICATION_FAIL);
                result.setMessage(MessageConstant.VERIFICATION_FAIL);
                result.setSuccess(false);
                return result;
            }
            userInfo.setPhone(mobile);
            if(clientUserInfoService.save(userInfo) > 0){
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
                result.setCode(CodeConstant.BIND_MOBILE_FAIL);
                result.setMessage(MessageConstant.BIND_MOBILE_FAIL);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode(CodeConstant.BIND_MOBILE_FAIL);
            result.setMessage(MessageConstant.BIND_MOBILE_FAIL);
            logger.error(MessageConstant.BIND_MOBILE_FAIL,e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changeMobile(String pin, String mobile, String msg) {
        RPCResult<Boolean> result = new RPCResult();
        try {
            if (StringUtils.isBlank(pin) || StringUtils.isBlank(msg)) {
                result.setCode(CodeConstant.PARAM_NULL);
                result.setMessage(MessageConstant.PARAM_NULL);
                result.setSuccess(false);
                return result;
            }
            if (!StringUtils.isMobileNO(mobile)) {
                result.setCode(CodeConstant.PARAM_FORMAT_ERROR);
                result.setMessage(MessageConstant.PARAM_FORMAT_ERROR);
                result.setSuccess(false);
                return result;
            }
            ClientUserInfo userInfo = clientUserInfoService.findByPin(pin);
            if (userInfo == null) {
                result.setCode(CodeConstant.USER_NULL);
                result.setMessage(MessageConstant.USER_NULL);
                result.setSuccess(false);
                return result;
            }

            String key = MessageFormat.format(MOBILE_USER_CHANGE, mobile);
            String o = (String) redisTemplate.opsForValue().get(key);
            if(o == null){
                result.setCode(CodeConstant.CODE_TIMEOUT);
                result.setMessage(MessageConstant.CODE_TIMEOUT);
                result.setSuccess(false);
                return result;
            }

            if(!o.equals(msg)){
                result.setCode(CodeConstant.VERIFICATION_FAIL);
                result.setMessage(MessageConstant.VERIFICATION_FAIL);
                result.setSuccess(false);
                return result;
            }

            userInfo.setPhone(mobile);
            if(clientUserInfoService.save(userInfo) > 0){
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
                result.setCode(CodeConstant.CHANGE_MOBILE_FAIL);
                result.setMessage(MessageConstant.CHANGE_MOBILE_FAIL);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode(CodeConstant.CHANGE_MOBILE_FAIL);
            result.setMessage(MessageConstant.CHANGE_MOBILE_FAIL);
            logger.error(MessageConstant.CHANGE_MOBILE_FAIL,e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changePass(String pin, String oldPass, String newPass) {
        RPCResult<Boolean> result = new RPCResult();
        try {
            if (StringUtils.isBlank(pin)) {
                result.setCode(CodeConstant.PARAM_NULL);
                result.setMessage(MessageConstant.PARAM_NULL);
                result.setSuccess(false);
                return result;
            }
            ClientUserInfo userInfo = clientUserInfoService.findByPin(pin);
            if (userInfo == null) {
                result.setCode(CodeConstant.USER_NULL);
                result.setMessage(MessageConstant.USER_NULL);
                result.setSuccess(false);
                return result;
            }

            oldPass = MD5.MD5Str(oldPass,passKey);
            if(!oldPass.equals(userInfo.getPasswd())){
                result.setCode(CodeConstant.PASS_ERROR);
                result.setMessage(MessageConstant.PASS_ERROR);
                result.setSuccess(false);
                return result;
            }

            userInfo.setPasswd(MD5.MD5Str(newPass,passKey));
            if(clientUserInfoService.save(userInfo) > 0){
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
                result.setCode(CodeConstant.CHANGE_PASS_FAIL);
                result.setMessage(MessageConstant.CHANGE_PASS_FAIL);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode(CodeConstant.CHANGE_PASS_FAIL);
            result.setMessage(MessageConstant.CHANGE_PASS_FAIL);
            logger.error(MessageConstant.CHANGE_PASS_FAIL,e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changePassByMobile(String pin, String mobile, String msg, String password) {
        RPCResult<Boolean> result = new RPCResult();
        try {
            if (StringUtils.isBlank(pin)) {
                result.setCode(CodeConstant.PARAM_NULL);
                result.setMessage(MessageConstant.PARAM_NULL);
                result.setSuccess(false);
                return result;
            }

            if (!StringUtils.isMobileNO(mobile)) {
                result.setCode(CodeConstant.PARAM_FORMAT_ERROR);
                result.setMessage(MessageConstant.PARAM_FORMAT_ERROR);
                result.setSuccess(false);
                return result;
            }
            ClientUserInfo userInfo = clientUserInfoService.findByPin(pin);
            if (userInfo == null) {
                result.setCode(CodeConstant.USER_NULL);
                result.setMessage(MessageConstant.USER_NULL);
                result.setSuccess(false);
                return result;
            }
            if(!mobile.equals(userInfo.getPhone())){
                result.setCode(CodeConstant.MOBILE_NOT_BELONG_USER);
                result.setMessage(MessageConstant.MOBILE_NOT_BELONG_USER);
                result.setSuccess(false);
                return result;
            }

            String key = MessageFormat.format(PASS_USER_CHANGE_BY_MOBILE, pin);
            String o = (String) redisTemplate.opsForValue().get(key);
            if(o == null){
                result.setCode(CodeConstant.CODE_TIMEOUT);
                result.setMessage(MessageConstant.CODE_TIMEOUT);
                result.setSuccess(false);
                return result;
            }
            if(!o.equals(msg)){
                result.setCode(CodeConstant.VERIFICATION_FAIL);
                result.setMessage(MessageConstant.VERIFICATION_FAIL);
                result.setSuccess(false);
                return result;
            }

            password = MD5.MD5Str(password,passKey);
            if(!password.equals(userInfo.getPasswd())){
                result.setCode(CodeConstant.PASS_ERROR);
                result.setMessage(MessageConstant.PASS_ERROR);
                result.setSuccess(false);
                return result;
            }
            userInfo.setPasswd(password);
            if(clientUserInfoService.save(userInfo) > 0){
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
                result.setCode(CodeConstant.CHANGE_PASS_FAIL);
                result.setMessage(MessageConstant.CHANGE_PASS_FAIL);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode(CodeConstant.CHANGE_PASS_FAIL);
            result.setMessage(MessageConstant.CHANGE_PASS_FAIL);
            logger.error(MessageConstant.CHANGE_PASS_FAIL,e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changePassByMobileBuildMsg(String pin, String mobile) {
        RPCResult<Boolean> result = new RPCResult();
        try {
            if (StringUtils.isBlank(pin)) {
                result.setCode(CodeConstant.PARAM_NULL);
                result.setMessage(MessageConstant.PARAM_NULL);
                result.setSuccess(false);
                return result;
            }
            if (!StringUtils.isMobileNO(mobile)) {
                result.setCode(CodeConstant.PARAM_FORMAT_ERROR);
                result.setMessage(MessageConstant.PARAM_FORMAT_ERROR);
                result.setSuccess(false);
                return result;
            }
            ClientUserInfo userInfo = clientUserInfoService.findByPin(pin);
            if (userInfo == null) {
                result.setCode(CodeConstant.USER_NULL);
                result.setMessage(MessageConstant.USER_NULL);
                result.setSuccess(false);
                return result;
            }
            if(!mobile.equals(userInfo.getPhone())){
                result.setCode(CodeConstant.MOBILE_NOT_BELONG_USER);
                result.setMessage(MessageConstant.MOBILE_NOT_BELONG_USER);
                result.setSuccess(false);
                return result;
            }

            String code = AliMsgUtil.randomSixCode();
            boolean res = AliMsgUtil.sendMsgSingle(mobile, code);
            if(res){
                redisTemplate.opsForValue().set(MessageFormat.format(LOGIN_MOBILE_CODE,pin),code,SysContant.MSGCODE_TIMEOUT,TimeUnit.SECONDS);
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
                result.setCode(CodeConstant.SEND_CODE_FAIL);
                result.setMessage(MessageConstant.SEND_CODE_FAIL);
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode(CodeConstant.SEND_CODE_FAIL);
            result.setMessage(MessageConstant.SEND_CODE_FAIL);
            logger.error(MessageConstant.SEND_CODE_FAIL,e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> forgetPass(String pin) {
        RPCResult<Boolean> result = new RPCResult();
        try {
            if (StringUtils.isBlank(pin)) {
                result.setCode(CodeConstant.PARAM_NULL);
                result.setMessage(MessageConstant.PARAM_NULL);
                result.setSuccess(false);
                return result;
            }

            ClientUserInfo userInfo = clientUserInfoService.findByPin(pin);
            if(userInfo == null){
                result.setSuccess(false);
                result.setCode(CodeConstant.USER_NULL);
                result.setMessage(MessageConstant.USER_NULL);
                return result;
            }
            String code = AliMsgUtil.randomSixCode();
            boolean res = AliMsgUtil.sendMsgSingle(userInfo.getPhone(), code);
            if(res){
                redisTemplate.opsForValue().set(MessageFormat.format(FORGET_PASS,pin),code,SysContant.MSGCODE_TIMEOUT,TimeUnit.SECONDS);
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
                result.setCode(CodeConstant.SEND_CODE_FAIL);
                result.setMessage(MessageConstant.SEND_CODE_FAIL);
            }
        }catch (Exception e){
            result.setSuccess(false);
            logger.error(MessageConstant.SEND_CODE_FAIL,e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> forgetPassCodeVerification(String pin, String code, String pass) {
        RPCResult<Boolean> result = new RPCResult();
        try {
            if (StringUtils.isBlank(pin)) {
                result.setCode(CodeConstant.PARAM_NULL);
                result.setMessage(MessageConstant.PARAM_NULL);
                result.setSuccess(false);
                return result;
            }

            ClientUserInfo userInfo = clientUserInfoService.findByPin(pin);
            if(userInfo == null){
                result.setSuccess(false);
                result.setCode(CodeConstant.USER_NULL);
                result.setMessage(MessageConstant.USER_NULL);
                return result;
            }

            String key = MessageFormat.format(FORGET_PASS, pin);
            String o = (String) redisTemplate.opsForValue().get(key);
            if(o == null){
                result.setCode(CodeConstant.CODE_TIMEOUT);
                result.setMessage(MessageConstant.CODE_TIMEOUT);
                result.setSuccess(false);
                return result;
            }
            if(!o.equals(code)){
                result.setCode(CodeConstant.VERIFICATION_FAIL);
                result.setMessage(MessageConstant.VERIFICATION_FAIL);
                result.setSuccess(false);
                return result;
            }

            pass = MD5.MD5Str(pass,passKey);
            userInfo.setPasswd(pass);
            if(clientUserInfoService.save(userInfo) > 0){
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
                result.setCode(CodeConstant.CHANGE_PASS_FAIL);
                result.setMessage(MessageConstant.CHANGE_PASS_FAIL);
            }
        }catch (Exception e){
            result.setSuccess(false);
            logger.error(MessageConstant.SEND_CODE_FAIL,e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changeNickName(String pin, String nickName) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            if(StringUtils.isBlank(pin) || StringUtils.isBlank(nickName)){
                result.setSuccess(false);
                result.setCode(CodeConstant.PARAM_NULL);
                result.setMessage(MessageConstant.PARAM_NULL);
                return result;
            }

            ClientUserInfo userInfo = clientUserInfoService.findByPin(pin);
            if(userInfo == null){
                result.setSuccess(false);
                result.setCode(CodeConstant.USER_NULL);
                result.setMessage(MessageConstant.USER_NULL);
                return result;
            }

            userInfo.setNickName(nickName);
            if(clientUserInfoService.save(userInfo) > 0){
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
                result.setCode(CodeConstant.CHANGE_NICK_FAIL);
                result.setMessage(MessageConstant.CHANGE_NICK_FAIL);
            }
        }catch (Exception e){
            result.setSuccess(false);
            result.setCode(CodeConstant.CHANGE_NICK_FAIL);
            result.setMessage(MessageConstant.CHANGE_NICK_FAIL);
            logger.error(MessageConstant.CHANGE_NICK_FAIL,e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changeSex(String pin, Integer sexType) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            if(StringUtils.isBlank(pin) || (sexType != SexEnum.MALE.getValue() && sexType != SexEnum.FEMALE.getValue())){
                result.setSuccess(false);
                result.setCode(CodeConstant.PARAM_NULL);
                result.setMessage(MessageConstant.PARAM_NULL);
                return result;
            }

            ClientUserInfo userInfo = clientUserInfoService.findByPin(pin);
            if(userInfo == null){
                result.setSuccess(false);
                result.setCode(CodeConstant.USER_NULL);
                result.setMessage(MessageConstant.USER_NULL);
                return result;
            }

            userInfo.setSexType(sexType);
            if(clientUserInfoService.save(userInfo) > 0){
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
                result.setCode(CodeConstant.CHANGE_SEX_FAIL);
                result.setMessage(MessageConstant.CHANGE_SEX_FAIL);

            }
        }catch (Exception e){
            result.setSuccess(false);
            result.setCode(CodeConstant.CHANGE_SEX_FAIL);
            result.setMessage(MessageConstant.CHANGE_SEX_FAIL);
            logger.error(MessageConstant.CHANGE_SEX_FAIL,e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changeBirthday(String pin, String date) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            if(StringUtils.isBlank(pin) || StringUtils.isBlank(date)){
                result.setSuccess(false);
                result.setCode(CodeConstant.PARAM_NULL);
                result.setMessage(MessageConstant.PARAM_NULL);
                return result;
            }

            ClientUserInfo userInfo = clientUserInfoService.findByPin(pin);
            if(userInfo == null){
                result.setSuccess(false);
                result.setCode(CodeConstant.USER_NULL);
                result.setMessage(MessageConstant.USER_NULL);
                return result;
            }
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = sdf.parse(date);
            userInfo.setBirthDay(d);
            if(clientUserInfoService.save(userInfo) > 0){
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
                result.setCode(CodeConstant.CHANGE_BIRTHDAY_FAIL);
                result.setMessage(MessageConstant.CHANGE_BIRTHDAY_FAIL);
            }
        }catch (Exception e){
            result.setSuccess(false);
            result.setCode(CodeConstant.CHANGE_BIRTHDAY_FAIL);
            result.setMessage(MessageConstant.CHANGE_BIRTHDAY_FAIL);
            logger.error(MessageConstant.CHANGE_BIRTHDAY_FAIL,e);
        }
        return result;
    }

    @Override
    public RPCResult<UserExtendDTO> findByUserCode(Integer userCode) {
        RPCResult<UserExtendDTO> result = new RPCResult<>();
        try{
            ClientUserExtendInfo clientUserExtendInfo = clientUserExtendInfoService.findByUserCode(userCode);
            if(clientUserExtendInfo == null){
                result.setSuccess(false);
                result.setMessage(MessageConstant.NOT_FIND_EXTEND_INFO);
                return result;
            }
            UserExtendDTO dto = new UserExtendDTO();
            BeanCoper.copyProperties(dto, clientUserExtendInfo);
            result.setData(dto);
            result.setSuccess(true);
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(MessageConstant.FIND_USER_EXTEND_INFO_FAIL);
            logger.error(MessageConstant.FIND_USER_EXTEND_INFO_FAIL,e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> saveUserExtendInfo(UserExtendDTO userExtendDTO) {
        RPCResult<Boolean> result = null;
        try{
            result = new RPCResult<>();
            ClientUserExtendInfo entity = new ClientUserExtendInfo();
            BeanCoper.copyProperties(entity, userExtendDTO);
            if(clientUserExtendInfoService.save(entity) > 0){
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
                result.setMessage(MessageConstant.SAVE_USER_EXTEND_INFO_FAIL);
            }
        }catch (Exception e){
            logger.error(MessageConstant.SAVE_USER_EXTEND_INFO_FAIL,e);
        }
        return result;
    }
}

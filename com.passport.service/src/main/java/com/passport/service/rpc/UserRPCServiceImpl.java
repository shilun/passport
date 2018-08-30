package com.passport.service.rpc;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.common.security.MD5;
import com.common.util.BeanCoper;
import com.common.util.RPCResult;
import com.common.util.StringUtils;
import com.common.util.model.SexEnum;
import com.common.util.model.YesOrNoEnum;
import com.passport.domain.ClientUserInfo;
import com.passport.rpc.UserRPCService;
import com.passport.rpc.dto.UserDTO;
import com.passport.service.ClientUserInfoService;
import com.passport.service.util.AliMsgUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@com.alibaba.dubbo.config.annotation.Service(version = "1.0.0")
public class UserRPCServiceImpl implements UserRPCService {

    private final Logger logger = Logger.getLogger(UserRPCServiceImpl.class);

    @Value("${app.passKey}")
    private String passKey;
    /**
     * 用户注册redis key
     */
    private final String PASS_USER_REG = "pass.user.reg.{0}";
    private final String PASS_USER_CHANGE_BY_MOBILE = "pass.user.change.by.mobile.{0}";
    private final String MOBILE_USER_CHANGE = "mobile.user.change.{0}";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Resource
    private ClientUserInfoService clientUserInfoService;

    @Override
    public RPCResult<Boolean> regist(String account) {
        RPCResult result = new RPCResult();
        if (StringUtils.isBlank(account)) {
            result.setCode("regist.error.account.blank");
            result.setMessage("账户不能为空");
            result.setSuccess(false);
            return result;
        }
        if (!StringUtils.isMobileNO(account)) {
            result.setCode("regist.error.account.format");
            result.setMessage("手机格式不正确");
            result.setSuccess(false);
            return result;
        }
        ClientUserInfo entity = clientUserInfoService.findByPhone(account);
        if (entity != null) {
            result.setCode("regist.error.account.dup");
            result.setMessage("手机号码重复");
            result.setSuccess(false);
            return result;
        }


        result.setSuccess(true);
        return result;
    }

    @Override
    public RPCResult<UserDTO> registVerification(String account, String vcode, String pass) {
        RPCResult<UserDTO> result = new RPCResult<>();
        try {
            String key = MessageFormat.format(PASS_USER_REG, account);
            String o = (String) redisTemplate.opsForValue().get(key);
            if (o.equalsIgnoreCase(vcode)) {
                result.setSuccess(true);
                ClientUserInfo entity = new ClientUserInfo();
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
            logger.error("验证注册失败", e);
        }
        result.setSuccess(false);
        result.setCode("registVerification.error");
        result.setMessage("注册验证失败");
        return result;
    }

    @Override
    public RPCResult<UserDTO> findByPin(String pin) {
        return null;
    }

    @Override
    public RPCResult<UserDTO> findByAccount(String account) {
        return null;
    }

    @Override
    public RPCResult<Boolean> loginCodeBuild(String account) {
        return null;
    }

    @Override
    public RPCResult<UserDTO> loginCodeBuildVerification(String account, String vcode) {
        return null;
    }

    @Override
    public RPCResult<UserDTO> login(String account, String passwrd) {
        return null;
    }

    @Override
    public RPCResult<Boolean> initPass(String pin, String passwd) {
        return null;
    }

    @Override
    public RPCResult<Boolean> changeMobileBuildMsg(String pin, String mobile) {
        return null;
    }

    @Override
    public RPCResult<Boolean> bindMobileBuildMsg(String pin, String mobile) {
        return null;
    }

    @Override
    public RPCResult<Boolean> bindMobile(String pin, String mobile, String msg) {
        return null;
    }

    @Override
    public RPCResult<Boolean> changeMobile(String pin, String mobile, String msg) {
        RPCResult result = new RPCResult();
        try {
            if (StringUtils.isBlank(pin) || StringUtils.isBlank(mobile) || StringUtils.isBlank(msg)) {
                result.setCode("changeMobile.error.param.blank");
                result.setMessage("param不能为空");
                result.setSuccess(false);
                return result;
            }
            ClientUserInfo userInfo = clientUserInfoService.findByPin(pin);
            if (userInfo == null) {
                result.setCode("user is null");
                result.setMessage("用户不存在");
                result.setSuccess(false);
                return result;
            }

            String key = MessageFormat.format(MOBILE_USER_CHANGE, pin);
            String o = (String) redisTemplate.opsForValue().get(key);
            if(o == null){
                result.setCode("get code error");
                result.setMessage("验证码过期");
                result.setSuccess(false);
                return result;
            }

            userInfo.setPhone(mobile);
            if(clientUserInfoService.save(userInfo) > 0){
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
                result.setCode("changeMobile.fail");
                result.setMessage("修改手机号失败");
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode("changePass.error");
            result.setMessage("修改手机号异常");
            logger.error("修改手机号异常",e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changePass(String pin, String oldPass, String newPass) {
        RPCResult result = new RPCResult();
        try {
            if (StringUtils.isBlank(pin)) {
                result.setCode("changePass.error.pin.blank");
                result.setMessage("pin不能为空");
                result.setSuccess(false);
                return result;
            }
            ClientUserInfo userInfo = clientUserInfoService.findByPin(pin);
            if (userInfo == null) {
                result.setCode("user is null");
                result.setMessage("用户不存在");
                result.setSuccess(false);
                return result;
            }

            oldPass = MD5.MD5Str(oldPass);
            if(!oldPass.equals(userInfo.getPasswd())){
                result.setCode("pass error");
                result.setMessage("密码错误");
                result.setSuccess(false);
                return result;
            }

            userInfo.setPasswd(MD5.MD5Str(newPass));
            if(clientUserInfoService.save(userInfo) > 0){
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
                result.setCode("changePass.fail");
                result.setMessage("修改密码失败");
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode("changePass.error");
            result.setMessage("修改密码异常");
            logger.error("修改密码异常",e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changePassByMobile(String pin, String mobile, String msg, String password) {
        RPCResult<Boolean> result = new RPCResult();
        try {
            if (StringUtils.isBlank(pin) || StringUtils.isMobileNO(mobile)) {
                result.setCode("forgetPass.error.param.blank");
                result.setMessage("参数不能为空");
                result.setSuccess(false);
                return result;
            }
            ClientUserInfo userInfo = clientUserInfoService.findByPin(pin);
            if (userInfo == null) {
                result.setCode("user is null");
                result.setMessage("用户不存在");
                result.setSuccess(false);
                return result;
            }
            if(!mobile.equals(userInfo.getPhone())){
                result.setCode("mobile error");
                result.setMessage("pin 与 mobile 不匹配");
                result.setSuccess(false);
                return result;
            }

            String key = MessageFormat.format(PASS_USER_CHANGE_BY_MOBILE, pin);
            String o = (String) redisTemplate.opsForValue().get(key);
            if(o == null){
                result.setCode("get code error");
                result.setMessage("验证码过期");
                result.setSuccess(false);
                return result;
            }
            if(!o.equals(msg)){
                result.setCode("code error");
                result.setMessage("验证码错误");
                result.setSuccess(false);
                return result;
            }

            password = MD5.MD5Str(password);
            if(!password.equals(userInfo.getPasswd())){
                result.setCode("pass error");
                result.setMessage("密码错误");
                result.setSuccess(false);
                return result;
            }
            userInfo.setPasswd(password);
            if(clientUserInfoService.save(userInfo) > 0){
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
                result.setCode("changePassByMobile.fail");
                result.setMessage("修改密码失败");
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode("changePassByMobile.error");
            result.setMessage("修改密码异常");
            logger.error("修改密码异常",e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changePassByMobileBuildMsg(String pin, String mobile) {
        RPCResult<Boolean> result = new RPCResult();
        try {
            if (StringUtils.isBlank(pin) || StringUtils.isMobileNO(mobile)) {
                result.setCode("forgetPass.error.param.blank");
                result.setMessage("参数不能为空");
                result.setSuccess(false);
                return result;
            }
            ClientUserInfo userInfo = clientUserInfoService.findByPin(pin);
            if (userInfo == null) {
                result.setCode("user is null");
                result.setMessage("用户不存在");
                result.setSuccess(false);
                return result;
            }
            if(!mobile.equals(userInfo.getPhone())){
                result.setCode("mobile error");
                result.setMessage("pin 与 mobile 不匹配");
                result.setSuccess(false);
                return result;
            }

            //TODO  生成短信验证码
            boolean res = AliMsgUtil.sendMsgSingle(mobile, AliMsgUtil.randomSixCode());
            if(res){
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
                result.setCode("changePassByMobileBuildMsg.error");
                result.setMessage("发送验证码失败");
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode("changePassByMobileBuildMsg.error");
            result.setMessage("发送验证码异常");
            logger.error("发送验证码异常",e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> forgetPass(String pin) {
        return null;
    }

    @Override
    public RPCResult<Boolean> forgetPassCodeVerification(String pin, String code, String pass) {
        return null;
    }

    @Override
    public RPCResult<Boolean> changeNickName(String pin, String nickName) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            if(StringUtils.isBlank(pin) || StringUtils.isBlank(nickName)){
                result.setSuccess(false);
                result.setCode("param.error");
                result.setMessage("参数错误");
                return result;
            }

            ClientUserInfo userInfo = clientUserInfoService.findByPin(pin);
            if(userInfo == null){
                result.setSuccess(false);
                result.setCode("user is null");
                result.setMessage("用户不存在");
                return result;
            }

            userInfo.setNickName(nickName);
            if(clientUserInfoService.save(userInfo) > 0){
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
                result.setCode("changeNickName.fail");
                result.setMessage("修改昵称失败");
            }
        }catch (Exception e){
            result.setSuccess(false);
            result.setCode("changeNickName.error");
            result.setMessage("修改昵称错误");
            logger.error("修改昵称异常",e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changeSex(String pin, Integer sexType) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            if(StringUtils.isBlank(pin) || (sexType != SexEnum.MALE.getValue() && sexType != SexEnum.FEMALE.getValue())){
                result.setSuccess(false);
                result.setCode("param.error");
                result.setMessage("参数错误");
                return result;
            }

            ClientUserInfo userInfo = clientUserInfoService.findByPin(pin);
            if(userInfo == null){
                result.setSuccess(false);
                result.setCode("user is null");
                result.setMessage("用户不存在");
                return result;
            }

            userInfo.setSexType(sexType);
            if(clientUserInfoService.save(userInfo) > 0){
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
                result.setCode("changeSex.fail");
                result.setMessage("修改性别失败");

            }
        }catch (Exception e){
            result.setSuccess(false);
            result.setCode("changeSex.error");
            result.setMessage("修改性别错误");
            logger.error("修改性别异常",e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changeBirthday(String pin, String date) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            if(StringUtils.isBlank(pin) || StringUtils.isBlank(date)){
                result.setSuccess(false);
                result.setCode("param.error");
                result.setMessage("参数错误");
                return result;
            }

            ClientUserInfo userInfo = clientUserInfoService.findByPin(pin);
            if(userInfo == null){
                result.setSuccess(false);
                result.setCode("user is null");
                result.setMessage("用户不存在");
                return result;
            }
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = sdf.parse(date);
            userInfo.setBirthDay(d);
            if(clientUserInfoService.save(userInfo) > 0){
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
                result.setCode("changeBirthday.fail");
                result.setMessage("修改生日失败");
            }
        }catch (Exception e){
            result.setSuccess(false);
            result.setCode("changeBirthday.error");
            result.setMessage("修改生日错误");
            logger.error("修改生日异常",e);
        }
        return result;
    }
}

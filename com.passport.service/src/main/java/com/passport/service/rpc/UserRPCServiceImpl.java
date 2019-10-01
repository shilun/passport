package com.passport.service.rpc;

import com.common.exception.BizException;
import com.common.rpc.StatusRpcServiceImpl;
import com.common.security.DesDecrypter;
import com.common.security.DesEncrypter;
import com.common.security.MD5;
import com.common.util.BeanCoper;
import com.common.util.RPCResult;
import com.common.util.StringUtils;
import com.common.util.model.YesOrNoEnum;
import com.passport.domain.ClientUserInfo;
import com.passport.rpc.UserRPCService;
import com.passport.rpc.dto.UserDTO;
import com.passport.service.ClientUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@org.apache.dubbo.config.annotation.Service
public class UserRPCServiceImpl extends StatusRpcServiceImpl implements UserRPCService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private ClientUserInfoService clientUserInfoService;

    private final String LOGIN_TOKEN = "passport.login.token.{0}";
    private final String LOGIN_PIN = "passport.login.{1}";
    @Value("${app.token.encode.key}")
    private String appTokenEncodeKey;
    @Value("${app.passKey}")
    private String passKey;

    @Override
    public RPCResult<UserDTO> login(String pin, String pass) {
        RPCResult<UserDTO> result = new RPCResult<>();
        try {
            if (StringUtils.isBlank(pass)) {
                throw new BizException("参数不能为空");
            }
            if (!StringUtils.isMobileNO(pin)) {
                throw new BizException("手机号格式不正确");
            }
            ClientUserInfo userInfo = clientUserInfoService.findByPin(pin);
            if (userInfo == null || userInfo.getStatus().intValue() == YesOrNoEnum.NO.getValue()) {
                throw new BizException("无法找到该用户,请注册");
            }
            pass = MD5.MD5Str(pass, passKey);
            if (!pass.equals(userInfo.getPasswd())) {
                throw new BizException("密码错误");
            }
            UserDTO userDTO = buildToken(userInfo);
            result.setData(userDTO);
            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            log.error("userrpc.login.error.%s.%s", new Object[]{pin, pass});
            result.setCode("login.error");
        }
        return result;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public RPCResult<UserDTO> loginOut(String token) {
        RPCResult<UserDTO> result = new RPCResult<>();
        try {
            String key = MessageFormat.format(LOGIN_TOKEN, token);
            UserDTO o = (UserDTO) redisTemplate.opsForValue().get(key);
            redisTemplate.delete(MessageFormat.format(LOGIN_PIN, o.getPin()));
            redisTemplate.delete(key);
            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            log.error("登出失败", e);
        }
        result.setSuccess(false);
        result.setCode("passport.login.out.error");
        result.setMessage("登出失败");
        return result;
    }

    @Override
    public RPCResult<UserDTO> findByPin(String pin) {
        RPCResult<UserDTO> result = new RPCResult<>();
        try {
            ClientUserInfo userInfo = clientUserInfoService.findByPin(pin);
            UserDTO userDTO = BeanCoper.copyProperties(UserDTO.class, userInfo);
            result.setData(userDTO);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("userrpc.findByPin.error.%s", pin, e);
            result.setCode("userrpc.findByPin.error");
            result.setMessage("查询用户pin失败");
        }
        return result;
    }


    @Override
    public RPCResult<Boolean> changePass(String pin, String pass) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            String loginPinKey = MessageFormat.format(LOGIN_PIN, pin);
            String token = (String) redisTemplate.opsForValue().get(loginPinKey);
            String tokenKey = MessageFormat.format(LOGIN_TOKEN, token);
            redisTemplate.delete(loginPinKey);
            redisTemplate.delete(tokenKey);
            clientUserInfoService.changePass(pin, pass);
            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            result.setCode("userrpc.changePass.error");
            result.setMessage("修改用户密码失败");
            log.error("userrpc.changePass.error.%s.%s", new Object[]{pin, pass});
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changePass(String token, String oldPass, String newPass) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            String tokenData = DesDecrypter.decryptString(token, appTokenEncodeKey);
            String[] data = tokenData.split(":");
            String redisTokenKey = MessageFormat.format(LOGIN_TOKEN, data[2]);
            UserDTO o = (UserDTO) redisTemplate.opsForValue().get(redisTokenKey);
            if (o != null) {
                redisTemplate.delete(redisTokenKey);
                redisTemplate.delete(MessageFormat.format(LOGIN_PIN, o.getPin()));
            }
            clientUserInfoService.changePass(token, oldPass, newPass);
            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            result.setCode("userrpc.changePass.error");
            result.setMessage("修改用户密码失败");
            log.error("userrpc.changePass.error.%s.%s", new Object[]{token, oldPass});
        }
        return result;
    }

    @Override
    public RPCResult<UserDTO> verificationToken(String token) {
        String tokenData = DesDecrypter.decryptString(token, appTokenEncodeKey);
        RPCResult<UserDTO> result = new RPCResult<>();
        String[] data = tokenData.split(":");
        String redisTokenKey = MessageFormat.format(LOGIN_TOKEN, data[2]);
        UserDTO userDTO = (UserDTO) redisTemplate.opsForValue().get(redisTokenKey);
        if (userDTO != null) {
            redisTemplate.expire(redisTokenKey, 1, TimeUnit.HOURS);
            redisTemplate.expire(MessageFormat.format(LOGIN_PIN, userDTO.getPin()), 1, TimeUnit.HOURS);
            result.setData(userDTO);
            (userDTO).setToken(token);
            result.setSuccess(true);
            return result;
        }
        result.setCode("token.error");
        result.setMessage("token失效");
        return result;
    }

    @SuppressWarnings("Duplicates")
    private UserDTO buildToken(ClientUserInfo userInfo) {
        String loginPinKey = MessageFormat.format(LOGIN_PIN, userInfo.getPin());
        Object o = redisTemplate.opsForValue().get(loginPinKey);
        if (o != null) {
            String oldTokenKey = o.toString();
            oldTokenKey = MessageFormat.format(LOGIN_TOKEN, oldTokenKey);
            redisTemplate.delete(oldTokenKey);
            redisTemplate.delete(loginPinKey);
        }
        String newToken = StringUtils.getUUID();
        UserDTO dto = new UserDTO();
        dto.setPin(userInfo.getPin());
        BeanCoper.copyProperties(dto, userInfo);
        dto.setToken(newToken);
        String newTokenKey = MessageFormat.format(LOGIN_TOKEN, newToken);
        redisTemplate.opsForValue().set(loginPinKey, newToken, 1, TimeUnit.HOURS);
        redisTemplate.opsForValue().set(newTokenKey, dto, 1, TimeUnit.HOURS);

        String token = dto.getProxyId() + ":" + dto.getPin() + ":" + dto.getToken();
        token = DesEncrypter.cryptString(token, appTokenEncodeKey);
        dto.setToken(token);
        return dto;
    }

}

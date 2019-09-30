package com.passport.service.rpc;

import com.common.exception.BizException;
import com.common.rpc.StatusRpcServiceImpl;
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
    public RPCResult<Boolean> changePass(String pin, String oldPass, String newPass) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            clientUserInfoService.changePass(pin, oldPass,newPass);
            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            result.setCode("userrpc.changePass.error");
            result.setMessage("修改用户密码失败");
            log.error("userrpc.changePass.error.%s.%s", new Object[]{pin, oldPass});
        }
        return result;
    }

    @Override
    public RPCResult<UserDTO> verdifyToken(String token) {
        return null;    }

    private UserDTO buildToken(ClientUserInfo userInfo) {
        String login_pin_key = MessageFormat.format(LOGIN_PIN, userInfo.getPin());
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

}

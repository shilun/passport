package com.passport.service.rpc;

import com.common.exception.BizException;
import com.common.rpc.StatusRpcServiceImpl;
import com.common.security.DesDecrypter;
import com.common.security.DesEncrypter;
import com.common.security.MD5;
import com.common.util.BeanCoper;
import com.common.util.RPCResult;
import com.common.util.StringUtils;
import com.passport.domain.AdminUserInfo;
import com.passport.domain.ClientUserInfo;
import com.passport.domain.RoleInfo;
import com.passport.rpc.AdminRPCService;
import com.passport.rpc.dto.UserDTO;
import com.passport.service.AdminUserInfoService;
import com.passport.service.RoleInfoService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


@org.apache.dubbo.config.annotation.Service
@org.springframework.stereotype.Service
@Slf4j
public class AdminRPCServiceImpl extends StatusRpcServiceImpl implements AdminRPCService {

    private final String LOGIN_TOKEN = "passport.adminLogin.token.{0}";
    private final String LOGIN_PIN = "passport.adminLogin.{1}";

    @Value("${app.passKey}")
    private String passKey;
    @Value("${app.token.encode.key}")
    private String appTokenEncodeKey;
    @Resource
    private AdminUserInfoService adminUserInfoService;

    @Resource
    private RoleInfoService roleInfoService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public RPCResult<UserDTO> login(String loginName, String password) {
        RPCResult<UserDTO> result = new RPCResult();
        try {
            AdminUserInfo login = adminUserInfoService.login(loginName, password);
            if (login == null) {
                return result;
            }
            UserDTO dto = buildToken(login);
            result.setData(dto);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("管理员登录失败", e);
            result.setSuccess(false);
            result.setCode("admin.login.error");
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> logOut(String token) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            String key = MessageFormat.format(LOGIN_TOKEN, token);
            UserDTO o = (UserDTO) redisTemplate.opsForValue().get(key);
            if (o != null) {
                redisTemplate.delete(key);
                redisTemplate.delete(MessageFormat.format(LOGIN_PIN, o.getPin()));
            }
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("admin.logOut.error", e);
            throw new BizException("admin.logOut.error", "用户登出失败");
        }
        return result;
    }

    @SuppressWarnings("Duplicates")
    private UserDTO buildToken(AdminUserInfo userInfo) {
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
        String tokenKey = MessageFormat.format(LOGIN_TOKEN, newToken);
        redisTemplate.opsForValue().set(loginPinKey, newToken, 30, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(tokenKey, dto, 30, TimeUnit.MINUTES);
        String token = dto.getPin() + ":" + dto.getToken();
        token = DesEncrypter.cryptString(token, appTokenEncodeKey);
        dto.setToken(token);
        return dto;
    }

    @Override
    public RPCResult<List<String>> queryAdminRoles(String pin) {
        RPCResult<List<String>> result = new RPCResult<>();
        try {
            AdminUserInfo info = adminUserInfoService.findByPin(pin);
            String[] roles = info.getRoles();
            List<String> list = new ArrayList<>();
            for (String roleId : roles) {
                RoleInfo roleInfo = roleInfoService.findById(roleId);
                Collections.addAll(list, roleInfo.getResources().split(";"));
            }
            result.setSuccess(true);
            result.setData(list);
            return result;
        } catch (Exception e) {
            log.error("查询角色失败", e);
            result.setSuccess(false);
            result.setCode("queryAdminRoles.error");
            result.setMessage("查询角色失败");
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changePass(String token, String oldPass, String newPass) {
        String oldToken=token;
        String[] tokenParmas = DesDecrypter.decryptString(token, appTokenEncodeKey).split(":");
        token = tokenParmas[1];
        String pin = tokenParmas[0];
        RPCResult<Boolean> result = new RPCResult<>();
        result.setSuccess(false);
        String key = MessageFormat.format(LOGIN_TOKEN, token);
        try {
            UserDTO o = (UserDTO) redisTemplate.opsForValue().get(key);
            if (o == null) {
                result.setCode("passport.changePass.token.error");
                result.setMessage("token失效，修改用户密码失败");
                return result;
            }
            AdminUserInfo admin = adminUserInfoService.findByPin(pin);
            oldPass = MD5.MD5Str(oldPass, passKey);
            if (!admin.getPasswd().equals(oldPass)) {
                result.setCode("passport.changePass.password.error");
                result.setMessage("旧密码错误，修改用户密码失败");
                return result;
            }
            AdminUserInfo upEntity = new AdminUserInfo();
            upEntity.setId(admin.getId());
            upEntity.setPasswd(MD5.MD5Str(newPass, passKey));
            adminUserInfoService.save(upEntity);
            loginOut(oldToken);
            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            log.error("修改密码失败", e);
        }
        result.setCode("passport.changePass.error");
        result.setMessage("修改用户密码失败");
        return result;
    }

    @Override
    public RPCResult<UserDTO> verificationToken(String token) {
        String[] tokenParmas = DesDecrypter.decryptString(token, appTokenEncodeKey).split(":");
        token = tokenParmas[1];
        String pin = tokenParmas[0];
        RPCResult<UserDTO> result = new RPCResult<>();
        String key = MessageFormat.format(LOGIN_TOKEN, token);
        try {
            UserDTO o = (UserDTO) redisTemplate.opsForValue().get(key);
            if (o != null) {
                redisTemplate.expire(key, 1, TimeUnit.HOURS);
                redisTemplate.expire(MessageFormat.format(LOGIN_PIN, pin), 1, TimeUnit.HOURS);
                result.setData(o);
                result.setSuccess(true);
                return result;
            }
        } catch (Exception e) {
            log.error("验证管理员用户失败", e);
        }
        result.setSuccess(false);
        result.setMessage("验证管理员失败");
        result.setCode("verificationToken.error");
        return result;
    }

    @Override
    public RPCResult<UserDTO> loginOut(String token) {
        String[] tokenParmas = DesDecrypter.decryptString(token, appTokenEncodeKey).split(":");
        token = tokenParmas[1];
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
}

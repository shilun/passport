package com.passport.service.rpc;

import com.common.rpc.StatusRpcServiceImpl;
import com.common.security.MD5;
import com.common.util.RPCResult;
import com.common.util.StringUtils;
import com.passport.domain.AdminUserInfo;
import com.passport.domain.RoleInfo;
import com.passport.rpc.AdminRPCService;
import com.passport.rpc.dto.UserDTO;
import com.passport.service.AdminUserInfoService;
import com.passport.service.RoleInfoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service()
@com.alibaba.dubbo.config.annotation.Service(interfaceClass =AdminRPCService.class,timeout = 4000)
public class AdminRPCServiceImpl extends StatusRpcServiceImpl implements AdminRPCService {


    private Logger logger = Logger.getLogger(AdminRPCServiceImpl.class);

    @Value("${app.passKey}")
    private String passKey;
    @Resource
    private AdminUserInfoService adminUserInfoService;

    @Resource
    private RoleInfoService roleInfoService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private final String ADMIN_LOGIN = "pass.admin.login.{0}";
    private final Integer ADMIN_LOGIN_TIME = 60 * 60 * 30;

    @Override
    public RPCResult<UserDTO> login(String loginName, String password) {
        RPCResult<UserDTO> result = new RPCResult<>();
        try {
            AdminUserInfo login = adminUserInfoService.login(loginName, password);
            UserDTO dto = new UserDTO();
            dto.setToken(StringUtils.getUUID());
            dto.setPin(login.getPin());
            dto.setEmail(login.getEmail());
            dto.setNickName(login.getName());
            String key = MessageFormat.format(ADMIN_LOGIN, dto.getToken());
            redisTemplate.opsForValue().set(key, dto, ADMIN_LOGIN_TIME, TimeUnit.MINUTES);
            result.setData(dto);
            result.setSuccess(true);
        } catch (Exception e) {
            logger.error("管理员登录失败", e);
            result.setSuccess(false);
            result.setCode("admin.login.error");
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(StringUtils.getUUID());
    }

    @Override
    public RPCResult<List<String>> queryAdminRoles(String pin) {
        RPCResult<List<String>> result = new RPCResult<>();
        try {
            AdminUserInfo info = adminUserInfoService.findByPin(pin);
            Long[] roles = info.getRoles();
            List<String> list = new ArrayList<>();
            for (Long roleId : roles) {
                RoleInfo roleInfo = roleInfoService.findById(roleId);
                Collections.addAll(list, roleInfo.getResources().split(";"));
            }
            result.setSuccess(true);
            result.setData(list);
            return result;
        } catch (Exception e) {
            logger.error("查询角色失败", e);
            result.setSuccess(false);
            result.setCode("queryAdminRoles.error");
            result.setMessage("查询角色失败");
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changePass(String token, String oldPass, String newPass) {
        RPCResult<Boolean> result = new RPCResult<>();
        result.setSuccess(false);
        String key = MessageFormat.format(ADMIN_LOGIN, token);
        try {
            UserDTO o = (UserDTO) redisTemplate.opsForValue().get(key);
            if (o == null) {
                result.setCode("passport.changePass.token.error");
                result.setMessage("token失效，修改用户密码失败");
                return result;
            }
            AdminUserInfo admin = adminUserInfoService.findByPin(o.getPin());
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
            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            logger.error("修改密码失败", e);
        }
        result.setCode("passport.changePass.error");
        result.setMessage("修改用户密码失败");
        return result;
    }

    @Override
    public RPCResult<UserDTO> verificationToken(String token) {
        RPCResult<UserDTO> result = new RPCResult<>();
        String key = MessageFormat.format(ADMIN_LOGIN, token);
        try {
            UserDTO o = (UserDTO) redisTemplate.opsForValue().get(key);
            if (o != null) {
                redisTemplate.opsForValue().set(key, o, ADMIN_LOGIN_TIME);
                result.setData(o);
                result.setSuccess(true);
                return result;
            }
        } catch (Exception e) {
            logger.error("验证管理员用户失败", e);
        }
        result.setSuccess(false);
        result.setMessage("验证管理员失败");
        result.setCode("verificationToken.error");
        return result;
    }

    @Override
    public RPCResult<UserDTO> loginOut(String token) {
        RPCResult<UserDTO> result = new RPCResult<>();
        try {
            String key = MessageFormat.format(ADMIN_LOGIN, token);
            redisTemplate.delete(key);
            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            logger.error("登出失败", e);
        }
        result.setSuccess(false);
        result.setCode("passport.login.out.error");
        result.setMessage("登出失败");
        return result;
    }
}

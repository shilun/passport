package com.passport.service.rpc;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.Format;
import java.text.MessageFormat;

@Service
@com.alibaba.dubbo.config.annotation.Service(version = "1.0.0")
public class UserRPCServiceImpl implements UserRPCService {


    @Value("${app.passKey}")
    private String passKey;
    /**
     * 用户注册redis key
     */
    private final String PASS_USER_REG = "pass.user.reg.{0}";

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
        return null;
    }

    @Override
    public RPCResult<Boolean> changePass(String pin, String oldPass, String newPass) {
        return null;
    }

    @Override
    public RPCResult<Boolean> changePassByMobile(String pin, String mobile, String msg, String password) {
        return null;
    }

    @Override
    public RPCResult<Boolean> changePassByMobileBuildMsg(String pin, String mobile) {
        return null;
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
        return null;
    }

    @Override
    public RPCResult<Boolean> changeSex(String pin, Integer sexType) {
        return null;
    }

    @Override
    public RPCResult<Boolean> changeBirthday(String pin, String date) {
        return null;
    }
}

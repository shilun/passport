package com.passport.service.rpc;

import com.common.exception.BizException;
import com.common.rpc.StatusRpcServiceImpl;
import com.common.security.DesDecrypter;
import com.common.security.DesEncrypter;
import com.common.security.MD5;
import com.common.util.BeanCoper;
import com.common.util.PageInfo;
import com.common.util.RPCResult;
import com.common.util.StringUtils;
import com.common.util.model.YesOrNoEnum;
import com.passport.domain.ClientUserInfo;
import com.passport.rpc.UserRPCService;
import com.passport.rpc.dto.UserDTO;
import com.passport.service.ClientUserInfoService;
import com.passport.service.constant.MessageConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@org.apache.dubbo.config.annotation.Service
public class UserRPCServiceImpl extends StatusRpcServiceImpl implements UserRPCService {

    private final Logger logger = LoggerFactory.getLogger(UserRPCServiceImpl.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private ClientUserInfoService clientUserInfoService;

    private final String LOGIN_TOKEN = "passport.login.token.{0}";
    private final String LOGIN_PIN = "passport.login.{0}.{1}";
    @Value("${app.token.encode.key}")
    private String appTokenEncodeKey;
    @Value("${app.passKey}")
    private String passKey;

    @Override
    public RPCResult<UserDTO> login(String phone, String pass) {
        return null;
    }

    @Override
    public RPCResult<UserDTO> findByPin(String pin) {
        return null;
    }



    @Override
    public RPCResult<Boolean> changePass(String pin, String pass) {
        return null;
    }

    @Override
    public RPCResult<Boolean> changePass(String pin, String oldPass, String newPass) {
        return null;
    }
}

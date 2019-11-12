package com.passport.service.rpc;

import com.common.exception.BizException;
import com.common.rpc.StatusRpcServiceImpl;
import com.common.security.DesDecrypter;
import com.common.security.DesEncrypter;
import com.common.security.MD5;
import com.common.util.BeanCoper;
import com.common.util.RPCResult;
import com.common.util.StringUtils;
import com.passport.domain.ProxyInfo;
import com.passport.domain.ProxyUserInfo;
import com.passport.rpc.ProxyInfoRPCService;
import com.passport.rpc.dto.ProxyDto;
import com.passport.rpc.dto.ProxyUserDto;
import com.passport.service.ProxyInfoService;
import com.passport.service.ProxyUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@org.apache.dubbo.config.annotation.Service
public class ProxyInfoRPCServiceImpl extends StatusRpcServiceImpl implements ProxyInfoRPCService {
    private final static String proxyUserLoginPin = "passport.proxy.{0}.pin.{1}";
    private final static String proxyUserLoginToken = "passport.proxy.{0}.token.{1}";

    @Value("${app.passKey}")
    private String passKey;

    @Value("${app.token.encode.key}")
    private String appTokenEncodeKey;

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ProxyInfoService proxyInfoService;

    @Resource
    private ProxyUserInfoService proxyUserInfoService;

    @Override
    public RPCResult<ProxyUserDto> login(String domain, String account, String pass) {
        RPCResult<ProxyUserDto> result = new RPCResult<>();
        try {
            ProxyUserInfo user = proxyUserInfoService.login(domain, account, pass);
            user.setPass(null);
            ProxyUserDto dto = BeanCoper.copyProperties(ProxyUserDto.class, user);
            dto.setToken(StringUtils.getUUID());
            String proxyId = dto.getProxyId();
            String loginPinKey = MessageFormat.format(proxyUserLoginPin, proxyId, dto.getPin());
            String loginTokenKey = MessageFormat.format(proxyUserLoginToken, proxyId, dto.getToken());
            stringRedisTemplate.opsForValue().set(loginTokenKey, dto.getPin(), 30, TimeUnit.MINUTES);
            ProxyUserDto userDto = (ProxyUserDto) redisTemplate.opsForValue().get(loginPinKey);
            if (userDto != null) {
                String oldLoginTokenKey = MessageFormat.format(proxyUserLoginToken, proxyId, userDto.getToken());
                redisTemplate.delete(oldLoginTokenKey);
            }
            redisTemplate.opsForValue().set(loginPinKey, dto, 30, TimeUnit.MINUTES);
            String token = dto.getProxyId() + ":" + dto.getPin() + ":" + dto.getToken();
            token = DesEncrypter.cryptString(token, appTokenEncodeKey);
            dto.setToken(token);
            result.setData(dto);
            result.setSuccess(true);
        } catch (BizException e) {
            result.setException(e);
        } catch (Exception e) {
            log.error("ProxyInfoRPCService.login domain={},account={},pass={}", new String[]{domain, account, pass}, e);
            result.setCode("system.error");
            result.setMessage("未知错误");
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> logOut(String token) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            String tokens[] = DesDecrypter.decryptString(token, appTokenEncodeKey).split(":");
            String proxyId = tokens[0];
            String pin = tokens[1];
            token = tokens[2];
            String loginPinKey = MessageFormat.format(proxyUserLoginPin, proxyId, pin);
            String loginTokenKey = MessageFormat.format(proxyUserLoginToken, proxyId, token);
            redisTemplate.delete(loginPinKey);
            redisTemplate.delete(loginTokenKey);
            result.setSuccess(true);
        } catch (BizException e) {
            result.setException(e);
        } catch (Exception e) {
            log.error("ProxyInfoRPCService.logOut token={}", token);
            result.setCode("system.error");
            result.setMessage("未知错误");
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> addUser(String proxyId, String pin, String phone, String pass, String desc, String resources[]) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            proxyUserInfoService.addUser(proxyId, pin, phone, pass, desc, resources);
            result.setSuccess(true);
        } catch (BizException e) {
            result.setException(e);
        } catch (Exception e) {
            log.error("ProxyInfoRPCService.addUser proxyId={},phone={},pass={},desc={},resources={}", new String[]{proxyId, phone, pass, desc, Arrays.toString(resources)}, e);
            result.setCode("system.error");
            result.setMessage("未知错误");
        }
        return result;
    }


    @Override
    public RPCResult<ProxyUserDto> verificationToken(String token) {
        RPCResult<ProxyUserDto> result = new RPCResult<>();
        try {
            String oldToken = token;
            String tokens[] = DesDecrypter.decryptString(token, appTokenEncodeKey).split(":");
            String proxyId = tokens[0];
            String pin = tokens[1];
            token = tokens[2];
            String loginPinKey = MessageFormat.format(proxyUserLoginPin, proxyId, pin);
            String loginTokenKey = MessageFormat.format(proxyUserLoginToken, proxyId, token);
            ProxyUserDto data = (ProxyUserDto) redisTemplate.opsForValue().get(loginPinKey);
            if (data != null) {
                redisTemplate.expire(loginTokenKey, 30, TimeUnit.MINUTES);
                redisTemplate.expire(loginPinKey, 30, TimeUnit.MINUTES);
                result.setSuccess(true);
                data.setToken(oldToken);
                result.setData(data);
                return result;
            }
        } catch (BizException e) {
            result.setException(e);
        } catch (Exception e) {
            log.error("ProxyInfoRPCService.verificationToken token={}", token, e);
            result.setCode("system.error");
            result.setMessage("未知错误");
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> initPass(String domain, String pin, String pass) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            proxyUserInfoService.initPass(domain, pin, pass);
            ProxyInfo proxyInfo = proxyInfoService.findByDomain(domain);
            String loginPinKey = MessageFormat.format(proxyUserLoginPin, proxyInfo.getId(), pin);
            ProxyUserDto userDto = (ProxyUserDto) redisTemplate.opsForValue().get(loginPinKey);
            if (userDto != null) {
                String oldLoginTokenKey = MessageFormat.format(proxyUserLoginToken, proxyInfo.getId(), userDto.getToken());
                redisTemplate.delete(oldLoginTokenKey);
                redisTemplate.delete(loginPinKey);
            }
            result.setSuccess(true);
        } catch (BizException e) {
            result.setException(e);
        } catch (Exception e) {
            log.error("ProxyInfoRPCService.changePass domain={},pin={},pass={}", new String[]{domain, pin, pass}, e);
            result.setCode("system.error");
            result.setMessage("未知错误");
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changePass(String token, String oldPass, String newPass) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            String tokens[] = DesDecrypter.decryptString(token, appTokenEncodeKey).split(":");
            String proxyId = tokens[0];
            String pin = tokens[1];
            token = tokens[2];
            String loginPinKey = MessageFormat.format(proxyUserLoginPin, proxyId, pin);
            ProxyUserDto userDto = (ProxyUserDto) redisTemplate.opsForValue().get(loginPinKey);
            if (userDto != null) {
                String oldLoginTokenKey = MessageFormat.format(proxyUserLoginToken, proxyId, token);
                redisTemplate.delete(oldLoginTokenKey);
                redisTemplate.delete(loginPinKey);
                ProxyUserInfo info = proxyUserInfoService.findByProxyIdAndPin(proxyId, pin);
                if (!oldPass.equals(MD5.MD5Str(info.getPass(), passKey))) {
                    throw new BizException("old.pass.error", "旧密码验证失败");
                }
                proxyUserInfoService.initPass(proxyId, pin, newPass);
            }
            //清除用户登录
            result.setSuccess(true);
        } catch (BizException e) {
            result.setException(e);
        } catch (Exception e) {
            log.error("ProxyInfoRPCService.changePass token={},oldPass={},newPass={}", new String[]{token, oldPass, newPass}, e);
            result.setCode("system.error");
            result.setMessage("未知错误");
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> upUser(String proxyId, String pin, String phone, String desc, String[] resources, Integer status) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            proxyUserInfoService.upUser(proxyId, pin, phone, desc, resources, status);
            result.setSuccess(true);
        } catch (BizException e) {
            result.setException(e);
        } catch (Exception e) {
            log.error("ProxyInfoRPCService.upUser proxyId={},pin={},phone={},desc={},resources={},status={}", new String[]{proxyId, pin, phone, desc, Arrays.toString(resources), status.toString()}, e);
            result.setCode("system.error");
            result.setMessage("未知错误");
        }
        return result;
    }

    @Override
    public RPCResult<ProxyDto> findByDomain(String domain) {
        RPCResult<ProxyDto> result = new RPCResult<>();
        try {
            ProxyInfo info = proxyInfoService.findByDomain(domain);
            ProxyDto dto = BeanCoper.copyProperties(ProxyDto.class, info);
            result.setData(dto);
            result.setSuccess(true);
        } catch (BizException e) {
            result.setException(e);
        } catch (Exception e) {
            log.error("ProxyInfoRPCService.findByDomain domain={}", domain, e);
            result.setCode("system.error");
            result.setMessage("未知错误");
        }
        return result;
    }

    @Override
    public RPCResult<ProxyDto> findById(String proxyId) {
        RPCResult<ProxyDto> result = new RPCResult<>();
        try {
            ProxyInfo info = proxyInfoService.findById(proxyId);
            ProxyDto dto = BeanCoper.copyProperties(ProxyDto.class, info);
            result.setData(dto);
            result.setSuccess(true);
        } catch (BizException e) {
            result.setException(e);
        } catch (Exception e) {
            log.error("ProxyInfoRPCService.findById proxyId={}", proxyId, e);
            result.setCode("system.error");
            result.setMessage("未知错误");
        }
        return result;
    }

    @Override
    public RPCResult<List<ProxyDto>> queryAll() {
        RPCResult<List<ProxyDto>> result = new RPCResult<>();
        try {
            Page<ProxyInfo> proxyInfos = proxyInfoService.queryByPage(new ProxyInfo(), PageRequest.of(0, 1000));
            List<ProxyDto> list = new ArrayList((int) proxyInfos.getTotalElements());
            for (ProxyInfo info : proxyInfos.getContent()) {
                ProxyDto proxyDto = BeanCoper.copyProperties(ProxyDto.class, info);
                list.add(proxyDto);
            }
            result.setData(list);
            result.setSuccess(true);
        } catch (BizException e) {
            result.setException(e);
        } catch (Exception e) {
            log.error("ProxyInfoRPCService.queryAll", e);
            result.setCode("system.error");
            result.setMessage("未知错误");
        }
        return result;
    }
}

package com.passport.service.rpc;

import com.alibaba.dubbo.config.annotation.Service;
import com.common.util.BeanCoper;
import com.common.util.RPCResult;
import com.common.util.StringUtils;
import com.passport.domain.LogRegisterInfo;
import com.passport.domain.ProxyInfo;
import com.passport.rpc.dto.DateType;
import com.passport.service.LogLoginService;
import com.passport.rpc.ProxyRpcService;
import com.passport.rpc.dto.ProxyDto;
import com.passport.service.LogRegisterService;
import com.passport.service.ProxyInfoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Date;

@Service(timeout = 1000)
@org.springframework.stereotype.Service
public class ProxyRpcServiceImpl implements ProxyRpcService {

    private final static Logger logger = Logger.getLogger(ProxyRpcServiceImpl.class);
    @Value("${app.passKey}")
    private String passKey;
    @Resource
    private ProxyInfoService proxyInfoService;
    @Resource
    private LogLoginService logLoginService;
    @Resource
    private LogRegisterService logRegisterService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public RPCResult refreshToken(Long proxyId) {
        RPCResult result = new RPCResult();
        try {
            ProxyInfo entity = new ProxyInfo();
            entity.setId(proxyId);
            entity.setToken(StringUtils.getUUID());
            proxyInfoService.save(entity);
            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            logger.error("刷新代理商token失败", e);
        }
        result.setSuccess(false);
        result.setCode("proxy.refreshToken.error");
        result.setMessage("刷新代理商token失败");
        return result;
    }

    @Override
    public RPCResult upEncodingKey(Long proxyId, String encodingKey) {
        RPCResult result = new RPCResult();
        try {
            ProxyInfo entity = new ProxyInfo();
            entity.setId(proxyId);
            entity.setEncodingKey(encodingKey);
            proxyInfoService.save(entity);
            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            logger.error("更新加密码串失败", e);
        }
        result.setSuccess(false);
        result.setCode("proxy.upEncodingKey.error");
        result.setMessage("更新加密码串失败");
        return result;
    }

    @Override
    public RPCResult<ProxyDto> findById(Long id) {
        RPCResult<ProxyDto> result = new RPCResult<>();
        try {
            ProxyInfo entity = new ProxyInfo();
            entity.setId(id);
            entity = proxyInfoService.findByOne(entity);
            ProxyDto dto = new ProxyDto();
            BeanCoper.copyProperties(dto, entity);
            result.setData(dto);
            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            logger.error("查找代理商失败", e);
        }
        result.setSuccess(false);
        result.setCode("proxy.find.error");
        result.setMessage("查找代理商失败");
        return result;
    }

    @Override
    public RPCResult<ProxyDto> findByDomain(String domain) {
        RPCResult<ProxyDto> result = new RPCResult<>();
        try {
            ProxyInfo query = new ProxyInfo();
            query.setDomain(domain);
            query = proxyInfoService.findByOne(query);
            ProxyDto dto = new ProxyDto();
            BeanCoper.copyProperties(dto, query);
            result.setData(dto);
            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            logger.error("查找代理商失败", e);
        }
        result.setSuccess(false);
        result.setCode("proxy.find.error");
        result.setMessage("查找代理商失败");
        return result;
    }

    @Override
    public RPCResult<ProxyDto> login(String account, String pass) {
        RPCResult<ProxyDto> result = new RPCResult<>();
        try {
            ProxyInfo proxyInfo = proxyInfoService.findByLoginName(account, pass);
            ProxyDto dto = new ProxyDto();
            BeanCoper.copyProperties(dto, proxyInfo);
            dto.setAccount(account);
            result.setData(dto);
            result.setSuccess(true);
        } catch (Exception e) {
            logger.error("登陆失败", e);
            result.setSuccess(false);
            result.setCode("proxy.login.error");
            result.setMessage("登陆失败");
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changePass(String account, String oldPass, String newPass) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            boolean flag = proxyInfoService.changePass(account, oldPass, newPass);
            result.setSuccess(flag);
        } catch (Exception e) {
            logger.error("修改密码失败", e);
            result.setSuccess(false);
            result.setCode("proxy.changePass.error");
            result.setMessage("修改密码失败");
        }
        return result;
    }

    @Override
    public RPCResult<Long> QueryActiveUsers(Date startTime, Date endTime) {
        RPCResult<Long> result = new RPCResult<>();
        try {
            if (startTime.getTime() > endTime.getTime()) {
                result.setSuccess(false);
                result.setMessage("时间错误");
                return result;
            }

            Long count = logLoginService.QueryActiveUsers(startTime, endTime);
            result.setSuccess(true);
            result.setData(count);
        } catch (Exception e) {
            logger.error("查询活跃人数异常", e);
            result.setSuccess(false);
            result.setCode("query.active.users.error");
            result.setMessage("查询活跃人数异常");
        }
        return result;
    }

    @Override
    public RPCResult<Long> QueryNewUsers(Date startTime, Date endTime) {
        RPCResult<Long> result = new RPCResult<>();
        try {
            if (startTime.getTime() > endTime.getTime()) {
                result.setSuccess(false);
                result.setMessage("时间错误");
                return result;
            }

            Long count = logRegisterService.QueryNewUsers(startTime, endTime);
            result.setSuccess(true);
            result.setData(count);
        } catch (Exception e) {
            logger.error("查询新增人数异常", e);
            result.setSuccess(false);
            result.setCode("query.new.users.error");
            result.setMessage("查询新增人数异常");
        }
        return result;
    }

    @Override
    public RPCResult<Long> QueryActiveUsers(DateType type) {
        return null;
    }

    @Override
    public RPCResult<Long> QueryNewUsers(DateType type) {
        return null;
    }
}

package com.passport.service.rpc;

import com.common.exception.BizException;
import com.common.rpc.StatusRpcServiceImpl;
import com.common.util.BeanCoper;
import com.common.util.RPCResult;
import com.passport.domain.ProxyInfo;
import com.passport.domain.ProxyUserInfo;
import com.passport.rpc.ProxyInfoRPCService;
import com.passport.rpc.dto.ProxyDto;
import com.passport.rpc.dto.ProxyUserDto;
import com.passport.service.ProxyInfoService;
import com.passport.service.ProxyUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;

@Slf4j
@Service
@org.apache.dubbo.config.annotation.Service
public class ProxyInfoRPCServiceImpl extends StatusRpcServiceImpl implements ProxyInfoRPCService {

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
            result.setData(dto);
            result.setSuccess(true);
        } catch (BizException e) {
            result.setMessage(e.getMessage());
            result.setCode(e.getCode());
        } catch (Exception e) {
            log.error("ProxyInfoRPCService.login domain={},account={},pass={}", new String[]{domain, account, pass});
            result.setCode("system.error");
            result.setMessage("未知错误");
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> addUser(String proxyId, String phone, String pass, String desc, String resources[]) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            proxyUserInfoService.addUser(proxyId, phone, pass, desc, resources);
        } catch (BizException e) {
            result.setMessage(e.getMessage());
            result.setCode(e.getCode());
        } catch (Exception e) {
            log.error("ProxyInfoRPCService.addUser proxyId={},phone={},pass={},desc={},resources={}", new String[]{proxyId, phone, pass, desc, Arrays.toString(resources)});
            result.setCode("system.error");
            result.setMessage("未知错误");
        }
        return result;

    }

    @Override
    public RPCResult<Boolean> logOut(String token) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            proxyUserInfoService.logOut(token);
        } catch (BizException e) {
            result.setMessage(e.getMessage());
            result.setCode(e.getCode());
        } catch (Exception e) {
            log.error("ProxyInfoRPCService.logOut token={}", token);
            result.setCode("system.error");
            result.setMessage("未知错误");
        }
        return result;
    }

    @Override
    public RPCResult<ProxyUserDto> verfiyToken(String token) {
        RPCResult<ProxyUserDto> result = new RPCResult<>();
        try {
            proxyUserInfoService.verfiyToken(token);
        } catch (BizException e) {
            result.setMessage(e.getMessage());
            result.setCode(e.getCode());
        } catch (Exception e) {
            log.error("ProxyInfoRPCService.verfiyToken token={}", token);
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
        } catch (BizException e) {
            result.setMessage(e.getMessage());
            result.setCode(e.getCode());
        } catch (Exception e) {
            log.error("ProxyInfoRPCService.initPass domain={},pin={},pass={}", new String[]{domain, pin, pass});
            result.setCode("system.error");
            result.setMessage("未知错误");
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changePass(String token, String oldPass, String newPass) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            proxyUserInfoService.changePass(token, oldPass, newPass);
        } catch (BizException e) {
            result.setMessage(e.getMessage());
            result.setCode(e.getCode());
        } catch (Exception e) {
            log.error("ProxyInfoRPCService.changePass token={},oldPass={},newPass={}", new String[]{token, oldPass, newPass});
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
        } catch (BizException e) {
            result.setMessage(e.getMessage());
            result.setCode(e.getCode());
        } catch (Exception e) {
            log.error("ProxyInfoRPCService.upUser proxyId={},pin={},phone={},desc={},resources={},status={}", new String[]{proxyId, pin, phone, desc, Arrays.toString(resources), status.toString()});
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
            result.setMessage(e.getMessage());
            result.setCode(e.getCode());
        } catch (Exception e) {
            log.error("ProxyInfoRPCService.findByDomain domain={}", domain);
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
            result.setMessage(e.getMessage());
            result.setCode(e.getCode());
        } catch (Exception e) {
            log.error("ProxyInfoRPCService.findById proxyId={}", proxyId);
            result.setCode("system.error");
            result.setMessage("未知错误");
        }
        return result;
    }
}

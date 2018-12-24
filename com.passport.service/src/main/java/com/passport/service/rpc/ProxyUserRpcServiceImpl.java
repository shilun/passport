package com.passport.service.rpc;

import com.common.exception.BizException;
import com.common.security.MD5;
import com.common.util.BeanCoper;
import com.common.util.RPCResult;
import com.common.util.StringUtils;
import com.common.util.model.YesOrNoEnum;
import com.passport.domain.ProxyUserInfo;
import com.passport.rpc.ProxyUserRpcService;
import com.passport.rpc.dto.ProxyUserDto;
import com.passport.service.ProxyUserInfoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@com.alibaba.dubbo.config.annotation.Service(interfaceClass = ProxyUserRpcService.class)
public class ProxyUserRpcServiceImpl implements ProxyUserRpcService {
    private static final Logger logger = Logger.getLogger(ProxyUserRpcServiceImpl.class);
    @Resource
    private ProxyUserInfoService proxyUserInfoService;

    private final String loginTokenKey = "passport.proxy.token.{0}";

    @Resource
    private RedisTemplate redisTemplate;

    @Value("${app.passKey}")
    private String passKey;


    @Override
    public RPCResult addUser(Long proxyId, String phone, String pass, String desc, Long roles[]) {
        RPCResult result = new RPCResult();
        try {
            if (proxyId == null) {
                result.setSuccess(false);
                result.setCode("proxyId.error");
                result.setMessage("代理商是标识不能为空");
                return result;
            }
            Long id = proxyUserInfoService.addUser(proxyId, phone, pass, desc);
            proxyUserInfoService.changeRole(proxyId, id, roles);
            result.setSuccess(true);
        } catch (Exception e) {
            logger.error("添加用户失败", e);
            result.setSuccess(false);
            result.setCode("addUser.error");
            result.setMessage("添加用户失败");
        }
        return result;
    }

    @Override
    public RPCResult<ProxyUserDto> login(Long proxyId, String account, String pass) {
        RPCResult<ProxyUserDto> result = new RPCResult<>();
        try {
            if (proxyId == null) {
                result.setSuccess(false);
                result.setCode("proxyId.error");
                result.setMessage("代理商是标识不能为空");
                return result;
            }
            ProxyUserInfo login = proxyUserInfoService.login(proxyId, account, pass);
            if (login == null) {
                result.setSuccess(false);
                result.setCode("account.Pass.error");
                result.setMessage("账户或密码错误");
                return result;
            }

            login.setPass(null);
            result.setSuccess(true);
            ProxyUserDto dto = BeanCoper.copyProperties(ProxyUserDto.class, login);

            dto.setAccount(account);
            dto.setLoginToken(StringUtils.getUUID());
            String loginPinToken = MessageFormat.format(loginTokenKey, dto.getLoginToken());
            redisTemplate.opsForValue().set(loginPinToken, dto, 1, TimeUnit.DAYS);
            result.setData(dto);

            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            logger.error("登录失败", e);
            result.setSuccess(false);
            result.setCode("login.error");
            result.setMessage("登录失败");
        }
        return result;
    }

    @Override
    public RPCResult logOut(String loginToken) {
        RPCResult result = new RPCResult();
        try {
            String loginPinToken = MessageFormat.format(loginTokenKey, loginToken);
            redisTemplate.delete(loginPinToken);
            result.setSuccess(true);
        } catch (Exception e) {
            logger.error("退出失败", e);
            result.setSuccess(false);
            result.setCode("logout.error");
            result.setMessage("退出失败");
        }
        return result;
    }

    @Override
    public RPCResult<ProxyUserDto> verfiyToken(String loginToken) {
        RPCResult<ProxyUserDto> result = new RPCResult<>();
        try {
            String loginPinToken = MessageFormat.format(loginTokenKey, loginToken);
            Object o = redisTemplate.opsForValue().get(loginPinToken);
            result.setData((ProxyUserDto) o);
            result.setSuccess(true);
        } catch (Exception e) {
            logger.error("验证失败", e);
            result.setSuccess(false);
            result.setCode("verfiyToken.error");
            result.setMessage("验证token失败");
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changePass(Long proxyId, String account, String oldPass, String newPass) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            if (proxyId == null) {
                result.setSuccess(false);
                result.setCode("proxyId.error");
                result.setMessage("代理商是标识不能为空");
                return result;
            }
            ProxyUserInfo query = new ProxyUserInfo();
            query.setProxyId(proxyId);
            boolean setLoginName = false;
            if (StringUtils.isMobileNO(account)) {
                setLoginName = true;
                query.setPhone(account);
            }
            if (setLoginName == false) {
                query.setPin(account);
            }
            query = proxyUserInfoService.findByOne(query);
            oldPass = MD5.MD5Str(oldPass, passKey);
            if (!oldPass.equalsIgnoreCase(query.getPass())) {
                throw new BizException("change.pass.error", "修改密码失败:旧密码失败");
            }

            newPass = MD5.MD5Str(newPass, passKey);
            Long userId = query.getId();
            query = new ProxyUserInfo();
            query.setId(userId);
            query.setPass(newPass);
            result.setData(true);
            proxyUserInfoService.save(query);
            result.setSuccess(true);
        } catch (Exception e) {
            logger.error("修改密码失败", e);
            result.setSuccess(false);
            result.setCode("change.pass.error");
            result.setMessage("修改密码失败");
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changeRole(Long proxyId, Long id, Long[] roles) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            if (proxyId == null) {
                result.setSuccess(false);
                result.setCode("proxyId.error");
                result.setMessage("代理商是标识不能为空");
                return result;
            }
            ProxyUserInfo entity = proxyUserInfoService.findById(id);
            if (entity.getProxyId().longValue() != proxyId.longValue()) {
                result.setSuccess(false);
                result.setCode("data.error");
                result.setMessage("数据失败");
                return result;
            }
            entity = new ProxyUserInfo();
            entity.setId(id);
            entity.setRoles(roles);
            proxyUserInfoService.save(entity);
            result.setSuccess(true);
            result.setData(true);
            return result;
        } catch (Exception e) {
            logger.error("修改角色失败", e);
            result.setSuccess(false);
            result.setCode("change.role.error");
            result.setMessage("修改角色失败");
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> upUser(Long proxyId, Long id, String phone, String desc, Integer status, Long roles[]) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            if (proxyId == null) {
                result.setSuccess(false);
                result.setCode("proxyId.error");
                result.setMessage("代理商是标识不能为空");
                return result;
            }
            ProxyUserInfo entity = proxyUserInfoService.findById(id);
            if (entity.getProxyId().longValue() != proxyId.longValue()) {
                result.setSuccess(false);
                result.setCode("data.error");
                result.setMessage("数据失败");
                return result;
            }
            proxyUserInfoService.upUser(proxyId, id, phone, desc, status);
            proxyUserInfoService.changeRole(proxyId, id, roles);
            result.setSuccess(true);
            result.setData(true);
            return result;
        } catch (Exception e) {
            logger.error("修改角色失败", e);
            result.setSuccess(false);
            result.setCode("change.role.error");
            result.setMessage("修改角色失败");
        }
        return result;
    }

    @Override
    public RPCResult<ProxyUserDto> find(Long proxyId, Long id) {
        RPCResult<ProxyUserDto> result = new RPCResult<>();
        try {
            if (proxyId == null) {
                result.setSuccess(false);
                result.setCode("proxyId.error");
                result.setMessage("代理商是标识不能为空");
                return result;
            }
            ProxyUserInfo login = proxyUserInfoService.findById(id);
            if (login == null) {
                result.setSuccess(false);
                result.setCode("data.error");
                result.setMessage("数据不存在");
                return result;
            }
            if (login.getProxyId().longValue() != proxyId.longValue()) {
                result.setSuccess(false);
                result.setCode("data.error");
                result.setMessage("数据不存在");
                return result;
            }
            login.setPass(null);
            result.setSuccess(true);
            ProxyUserDto dto = BeanCoper.copyProperties(ProxyUserDto.class, login);
            result.setData(dto);

            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            logger.error("查询失败", e);
            result.setSuccess(false);
            result.setCode("data.error");
            result.setMessage("查询失败");
        }
        return result;
    }

    @Override
    public RPCResult<List<ProxyUserDto>> queryByProxyId(Long proxyId) {
        RPCResult<List<ProxyUserDto>> result = new RPCResult<>();
        try {
            if (proxyId == null) {
                result.setSuccess(false);
                result.setCode("proxyId.error");
                result.setMessage("代理商是标识不能为空");
                return result;
            }
            ProxyUserInfo query = new ProxyUserInfo();
            query.setProxyId(proxyId);
            query.setStatus(YesOrNoEnum.YES.getValue());
            List<ProxyUserInfo> list = proxyUserInfoService.query(query);
            List<ProxyUserDto> resultList = new ArrayList<>();
            for (ProxyUserInfo info : list) {
                info.setPass("");
                resultList.add(BeanCoper.copyProperties(ProxyUserDto.class, info));
            }
            result.setData(resultList);
            result.setSuccess(true);
        } catch (Exception e) {
            logger.error("查询失败", e);
            result.setCode("queryByProxyId.error");
            result.setMessage("查询代理商运营用户失败");
        }
        return result;
    }
}

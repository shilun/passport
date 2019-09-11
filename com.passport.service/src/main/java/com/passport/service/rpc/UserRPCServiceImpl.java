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
import com.passport.domain.module.UserStatusEnum;
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
    public RPCResult<UserDTO> registerByDeviceId(Long proxyId, String deviceId, String agentType) {
        RPCResult<UserDTO> result = new RPCResult<>();
        try {
            UserDTO userDto = null;
            if (StringUtils.isBlank(deviceId)) {
                throw new BizException("guestReg.error", "游客注册失败");
            }
            ClientUserInfo entity = new ClientUserInfo();
            entity.setDeviceUid(deviceId);
            entity.setProxyId(proxyId);
            entity = clientUserInfoService.findByOne(entity);
            if (entity == null) {
                userDto = clientUserInfoService.registGuest(proxyId, deviceId, "");
            } else {
                userDto = clientUserInfoService.loginByDeviceUid(proxyId, deviceId);
            }
            result.setData(userDto);
            result.setSuccess(true);
            return result;
        } catch (Exception e) {
            result.setCode("registerByDeviceId.error");
            result.setMessage("注册设备失败");
        }
        return result;

    }


    public UserDTO loginByDeviceUid(Long proxyId, String deviceUid, String ip) {
        ClientUserInfo query = new ClientUserInfo();
        query.setStatus(UserStatusEnum.Normal.getValue());
        query.setProxyId(proxyId);
        query.setDeviceUid(deviceUid);
        query.setDelStatus(YesOrNoEnum.NO.getValue());
        ClientUserInfo userInfo = clientUserInfoService.findByOne(query);
        return freshUserDTO(proxyId, ip, userInfo);
    }

    private UserDTO freshUserDTO(Long proxyId, String ip, ClientUserInfo userInfo) {
        String login_pin_key = MessageFormat.format(LOGIN_PIN, proxyId, userInfo.getPin());
        Object o = redisTemplate.opsForValue().get(login_pin_key);
        if (o != null) {
            String oldTokenKey = o.toString();
            oldTokenKey = MessageFormat.format(LOGIN_TOKEN, oldTokenKey);
            redisTemplate.delete(oldTokenKey);
            redisTemplate.delete(login_pin_key);
        }
        String newToken = StringUtils.getUUID();
        UserDTO dto = new UserDTO();
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


    @Override
    public RPCResult<UserDTO> findByMobile(Long proxyId, String mobile) {
        RPCResult<UserDTO> rpcResult = new RPCResult<>();
        try {
            if (proxyId == null || StringUtils.isBlank(mobile)) {
                rpcResult.setSuccess(false);
                rpcResult.setCode("find.userDTO.null");
                rpcResult.setMessage(MessageConstant.FIND_USER_FAIL);
                return rpcResult;
            }
            ClientUserInfo userInfo = clientUserInfoService.findByPhone(proxyId, mobile);
            if (userInfo == null) {
                rpcResult.setSuccess(false);
                rpcResult.setCode("find.userDTO.null");
                rpcResult.setMessage(MessageConstant.FIND_USER_FAIL);
                return rpcResult;
            }
            UserDTO dto = new UserDTO();
            BeanCoper.copyProperties(dto, userInfo);
            rpcResult.setSuccess(true);
            rpcResult.setCode("find.userDTO.success");
            rpcResult.setMessage("获取用户成功");
            rpcResult.setData(dto);
            return rpcResult;
        } catch (Exception e) {
            rpcResult.setSuccess(false);
            rpcResult.setMessage(MessageConstant.FIND_USER_FAIL);
            rpcResult.setCode("find.userDTO.error");
            logger.error(MessageConstant.FIND_USER_FAIL, e);
        }
        return rpcResult;
    }

    private String PROXY_PIN = "passport.findByPin.proxy:{0}.pin:{1}";

    @Override
    public RPCResult<UserDTO> findByPin(Long proxyId, String pin) {
        String key = MessageFormat.format(PROXY_PIN, proxyId, pin);
        RPCResult<UserDTO> rpcResult = new RPCResult<>();
        try {
            if (StringUtils.isBlank(pin)) {
                rpcResult.setSuccess(false);
                rpcResult.setCode("find.userDTO.pin.null");
                rpcResult.setMessage(MessageConstant.FIND_USER_FAIL);
                return rpcResult;
            }
            ClientUserInfo userInfo = clientUserInfoService.findByPin(proxyId, pin);
            if (userInfo == null) {
                rpcResult.setSuccess(false);
                rpcResult.setCode("find.userDTO.null");
                rpcResult.setMessage(MessageConstant.FIND_USER_FAIL);
                return rpcResult;
            }
            UserDTO dto = new UserDTO();
            BeanCoper.copyProperties(dto, userInfo);
            rpcResult.setSuccess(true);
            rpcResult.setCode("find.userDTO.success");
            rpcResult.setMessage("获取用户成功");
            rpcResult.setData(dto);
            return rpcResult;
        } catch (Exception e) {
            rpcResult.setSuccess(false);
            rpcResult.setMessage(MessageConstant.FIND_USER_FAIL);
            rpcResult.setCode("find.userDTO.error");
            logger.error(MessageConstant.FIND_USER_FAIL, e);
        }
        return rpcResult;
    }


    @Override
    public RPCResult<UserDTO> verfiyToken(String token) {

        RPCResult<UserDTO> rpcResult = new RPCResult<>();
        try {
            String oldToken = token;
            token = DesDecrypter.decryptString(token, appTokenEncodeKey);
            token = token.split(":")[2];
            if (StringUtils.isBlank(token)) {
                rpcResult.setSuccess(false);
                rpcResult.setCode("find.userDTO.token.null");
                rpcResult.setMessage(MessageConstant.FIND_USER_BY_TOKEN);
                return rpcResult;
            }
            String tokenKey = MessageFormat.format(LOGIN_TOKEN, token);
            UserDTO dto = (UserDTO) redisTemplate.opsForValue().get(tokenKey);
            if (dto == null) {
                rpcResult.setSuccess(false);
                rpcResult.setCode("find.userDTO.dto.null");
                rpcResult.setMessage(MessageConstant.FIND_USER_FAIL);
                return rpcResult;
            }
            redisTemplate.opsForValue().set(tokenKey, dto, 7, TimeUnit.DAYS);
            rpcResult.setSuccess(true);
            dto.setToken(oldToken);
            rpcResult.setData(dto);
            return rpcResult;
        } catch (Exception e) {
            rpcResult.setSuccess(false);
            rpcResult.setCode("find.userDTO.dto.error");
            rpcResult.setMessage("查找用户失败");
            logger.error("查找用户失败", e);
        }
        return rpcResult;
    }


    /**
     * 获取全部玩家
     *
     * @param dto
     * @return
     */
    @Override
    public RPCResult<List<UserDTO>> query(UserDTO dto) {
        RPCResult<List<UserDTO>> result = new RPCResult<>();
        try {
            ClientUserInfo entity = new ClientUserInfo();
            BeanCoper.copyProperties(entity, dto);
            PageInfo pageinfo = dto.getPageinfo();
            if (pageinfo.getSize() == null) {
                pageinfo.setSize(10);
            }
            Pageable page = pageinfo.getPage();
            Page<ClientUserInfo> pages = clientUserInfoService.queryByPage(entity, page);

            List<ClientUserInfo> list = pages.getContent();
            result.setTotalPage(pages.getTotalPages());
            result.setPageSize(page.getPageSize());
            result.setPageIndex(page.getPageNumber());
            result.setTotalCount((int) pages.getTotalElements());

            List<UserDTO> userDTOs = new ArrayList<>();
            for (ClientUserInfo item : list) {
                UserDTO dto1 = new UserDTO();
                BeanCoper.copyProperties(dto1, item);
                userDTOs.add(dto1);
            }
            result.setSuccess(true);
            result.setCode("find.userDTO.dto.success");
            result.setMessage("获取用户成功");
            result.setData(userDTOs);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode("find.userDTO.dto.error");
            result.setMessage(MessageConstant.FIND_USER_EXTEND_INFO_FAIL);
            logger.error(MessageConstant.FIND_USER_EXTEND_INFO_FAIL, e);
        }
        return result;
    }


    @Override
    public RPCResult<Boolean> changeInfo(UserDTO dto) {
        RPCResult<Boolean> result = null;
        try {
            result = new RPCResult<>();
            Long proxyId = dto.getProxyId();
            String pin = dto.getPin();
            if (proxyId == null || StringUtils.isBlank(pin)) {
                result.setSuccess(false);
                result.setCode("param.null");
                return result;
            }

            ClientUserInfo info = clientUserInfoService.findByPin(proxyId, pin);
            if (info == null) {
                result.setSuccess(false);
                result.setCode("find.result.null");
                return result;
            }
            BeanCoper.copyProperties(info, dto);
            clientUserInfoService.save(info);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode("changeInfo.error");
            logger.error("", e);
        }
        return result;
    }

    @Override
    public RPCResult<Long> queryUsersCount(UserDTO dto) {
        RPCResult<Long> result = null;
        try {
            result = new RPCResult<>();
            ClientUserInfo clientUserInfo = new ClientUserInfo();
            BeanCoper.copyProperties(clientUserInfo, dto);
            Long res = clientUserInfoService.queryCount(clientUserInfo);
            result.setSuccess(true);
            result.setData(res);
        } catch (Exception e) {
            logger.error("查询异常", e);
            result.setSuccess(false);
            result.setCode("query.user.count.error");
            result.setMessage("查询异常");
        }
        return result;
    }


    @Override
    public RPCResult<Boolean> resetPass(String pin, String pass) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            ClientUserInfo clientUserInfo = clientUserInfoService.findById(pin);
            String id = clientUserInfo.getId();
            if (clientUserInfo == null) {
                result.setSuccess(false);
                result.setMessage("用户不存在");
                return result;
            }
            clientUserInfo = new ClientUserInfo();
            clientUserInfo.setId(id);
            clientUserInfo.setPasswd(MD5.MD5Str(pass, passKey));
            clientUserInfoService.save(clientUserInfo);
            result.setSuccess(true);
            result.setMessage("重置密码成功");
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode("resetPass.error");
            result.setMessage("重置密码失败");
            logger.error("添加用户失败", e);
        }
        return result;
    }

}

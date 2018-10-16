package com.passport.service.rpc;

import com.common.security.DesDecrypter;
import com.common.util.BeanCoper;
import com.common.util.RPCResult;
import com.common.util.StringUtils;
import com.passport.domain.ClientUserExtendInfo;
import com.passport.domain.ClientUserInfo;
import com.passport.domain.LogLoginInfo;
import com.passport.rpc.UserRPCService;
import com.passport.rpc.dto.*;
import com.passport.service.ClientUserExtendInfoService;
import com.passport.service.ClientUserInfoService;
import com.passport.service.LogLoginService;
import com.passport.service.constant.MessageConstant;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@com.alibaba.dubbo.config.annotation.Service(timeout = 1000)
public class UserRPCServiceImpl implements UserRPCService {

    private final Logger logger = Logger.getLogger(UserRPCServiceImpl.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private ClientUserInfoService clientUserInfoService;
    @Resource
    private ClientUserExtendInfoService clientUserExtendInfoService;
    @Resource
    private LogLoginService logLoginService;

    private final String LOGIN_TOKEN = "passport.login.token.{0}";
    @Value("${app.token.encode.key}")
    private String appTokenEncodeKey;

    @Override
    public RPCResult<UserExtendDTO> findByUserCode(Long proxyId, Integer userCode) {
        RPCResult<UserExtendDTO> rpcResult = new RPCResult<>();
        try {
            ClientUserExtendInfo clientUserExtendInfo = clientUserExtendInfoService.findByUserCode(userCode);
            UserExtendDTO dto = new UserExtendDTO();
            BeanCoper.copyProperties(dto, clientUserExtendInfo);
            rpcResult.setSuccess(true);
            rpcResult.setMessage("获取用户信息成功");
            rpcResult.setCode("find.userExtend.success");
            rpcResult.setData(dto);
            return rpcResult;
        } catch (Exception e) {
            rpcResult.setSuccess(false);
            rpcResult.setCode("find.userExtend.error");
            rpcResult.setMessage(MessageConstant.FIND_USER_EXTEND_INFO_FAIL);
            logger.error(MessageConstant.FIND_USER_EXTEND_INFO_FAIL, e);
        }
        return rpcResult;
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

    @Override
    public RPCResult<UserDTO> findByPin(Long proxyId, String pin) {
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

    public static void main(String[] args) {
        String text = "RJ73VLB6IN2NF7W4PH26GS3ZNNMMN2GYSOR6PUBXPPSMYJTTETGAJEKQ53GBL66L45TZJZ6HRROCDLEZ4FQVKP3VCXSH7BK6UQPZTVPV42Q2SOBXUMOQ";
        text = DesDecrypter.decryptString(text, "");
        System.out.println(text);
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
            rpcResult.setCode("find.userDTO.dto.success");
            rpcResult.setMessage("获取用户成功");
            dto.setToken(oldToken);
            rpcResult.setData(dto);
            return rpcResult;
        } catch (Exception e) {
            rpcResult.setSuccess(false);
            rpcResult.setCode("find.userDTO.dto.error");
            rpcResult.setMessage(MessageConstant.FIND_USER_FAIL);
            logger.error(MessageConstant.FIND_USER_BY_TOKEN, e);
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
            if (dto.getPageinfo().getSize() == null) {
                dto.getPageinfo().setSize(10);
            }
            Page<ClientUserInfo> pages = clientUserInfoService.queryByPage(entity, dto.getPageinfo().getPage());
            List<ClientUserInfo> list = pages.getContent();
            result.setTotalPage(pages.getTotalPages());
            result.setPageSize(dto.getPageinfo().getPage().getPageSize());
            result.setPageIndex(dto.getPageinfo().getPage().getPageNumber());
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
    public RPCResult<QipaiUserDTO> qipaiVerfiyToken(String token) {
        RPCResult<QipaiUserDTO> result = new RPCResult<>();
        try {
            String oldToken = token;
            token = DesDecrypter.decryptString(token, appTokenEncodeKey);
            String realToken = token.split(":")[2];
            if (StringUtils.isBlank(realToken)) {
                result.setSuccess(false);
                result.setCode("token.null");
                return result;
            }

            String tokenKey = MessageFormat.format(LOGIN_TOKEN, realToken);
            UserDTO dto = (UserDTO) redisTemplate.opsForValue().get(tokenKey);
            if (dto == null) {
                result.setSuccess(false);
                result.setCode("token.error");
                return result;
            }
            dto.setToken(oldToken);
            QipaiUserDTO qipaiUserDTO = new QipaiUserDTO();
            BeanCoper.copyProperties(qipaiUserDTO, dto);

            RPCResult<UserExtendDTO> extendResult = this.findByUserCode(dto.getProxyId(), dto.getId().intValue());
            qipaiUserDTO.setUserExtendDTO(extendResult.getData());
            result.setSuccess(true);
            result.setData(qipaiUserDTO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode("qipaiVerfiyToken.error");
            logger.error("", e);
        }
        return result;
    }

    @Override
    public RPCResult<LogLoginDto> getUserLastLoginInfo(Long proxyId, String pin) {
        RPCResult<LogLoginDto> result = null;
        try {
            result = new RPCResult<>();
            if (proxyId == null || StringUtils.isBlank(pin)) {
                result.setSuccess(false);
                result.setCode("param.null");
                return result;
            }
            LogLoginInfo info = logLoginService.getUserLastLoginInfo(proxyId, pin);
            if (info == null) {
                result.setSuccess(false);
                result.setCode("find.result.null");
                return result;
            }
            LogLoginDto dto = new LogLoginDto();
            BeanCoper.copyProperties(dto, info);
            result.setSuccess(true);
            result.setData(dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode("getUserLastLoginInfo.error");
            logger.error("", e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changeInfo(Long proxyId, String pin, ChangeInfoType type, String value) {
        RPCResult<Boolean> result = null;
        try {
            result = new RPCResult<>();
            if (proxyId == null || StringUtils.isBlank(pin) || StringUtils.isBlank(value)) {
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
            switch (type) {
                case NICK:
                    info.setNickName(value);
                    break;
                default:
                    result.setSuccess(false);
                    result.setCode("type.error");
            }
            clientUserInfoService.save(info);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode("changeInfo.error");
            logger.error("", e);
        }
        return result;
    }
}

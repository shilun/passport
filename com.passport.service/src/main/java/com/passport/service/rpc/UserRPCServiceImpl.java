package com.passport.service.rpc;

import com.common.security.DesDecrypter;
import com.common.security.DesEncrypter;
import com.common.util.BeanCoper;
import com.common.util.RPCResult;
import com.common.util.StringUtils;
import com.passport.domain.ClientUserExtendInfo;
import com.passport.domain.ClientUserInfo;
import com.passport.rpc.UserRPCService;
import com.passport.rpc.dto.QipaiUserDTO;
import com.passport.rpc.dto.UserDTO;
import com.passport.rpc.dto.UserExtendDTO;
import com.passport.service.ClientUserExtendInfoService;
import com.passport.service.ClientUserInfoService;
import com.passport.service.constant.MessageConstant;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    private final String LOGIN_TOKEN = "passport.login.token.{1}";
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


    @Override
    public RPCResult<UserDTO> verfiyToken(String token) {

        RPCResult<UserDTO> rpcResult = new RPCResult<>();
        try {
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
            redisTemplate.expire(tokenKey,7, TimeUnit.DAYS);
            rpcResult.setSuccess(true);
            rpcResult.setCode("find.userDTO.dto.success");
            rpcResult.setMessage("获取用户成功");
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
    public RPCResult<Page<UserDTO>> query(UserDTO dto) {
        RPCResult<Page<UserDTO>> result = new RPCResult<>();
        try {
            List<UserDTO> userDTOs = new ArrayList<>();
            ClientUserInfo entity = new ClientUserInfo();
            BeanCoper.copyProperties(entity, dto);
            Page<ClientUserInfo> page = clientUserInfoService.queryByPage(entity, dto.getPageinfo().getPage());
            for (ClientUserInfo clientUserInfo : page) {
                UserDTO userDTO = new UserDTO();
                BeanCoper.copyProperties(userDTO, clientUserInfo);
                userDTOs.add(userDTO);
            }
            Page<UserDTO> users = new PageImpl<>(userDTOs, dto.getPageinfo().getPage(), dto.getPageinfo().getSize());

            result.setSuccess(true);
            result.setCode("find.userDTO.dto.success");
            result.setMessage("获取用户成功");
            result.setData(users);
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
            token = DesDecrypter.decryptString(token, appTokenEncodeKey);
            token = token.split(":")[2];
            if (StringUtils.isBlank(token)) {
                result.setSuccess(false);
                result.setCode("token.null");
                return result;
            }

            String tokenKey = MessageFormat.format(LOGIN_TOKEN, token);
            UserDTO dto = (UserDTO) redisTemplate.opsForValue().get(tokenKey);
            if (dto == null) {
                result.setSuccess(false);
                result.setCode("token.error");
                return result;
            }
            QipaiUserDTO qipaiDTO = new QipaiUserDTO();
            qipaiDTO.setUserDTO(dto);
            RPCResult<UserExtendDTO> extendResult = this.findByUserCode(dto.getProxyId(), dto.getId().intValue());
            qipaiDTO.setUserExtendDTO(extendResult.getData());
            result.setSuccess(true);
            result.setData(qipaiDTO);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode("qipaiVerfiyToken.error");
            logger.error("", e);
        }
        return result;
    }
}

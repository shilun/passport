package com.passport.service.rpc;

import com.common.security.DesDecrypter;
import com.common.security.MD5;
import com.common.util.*;
import com.common.util.model.SexEnum;
import com.common.util.model.YesOrNoEnum;
import com.passport.domain.ClientUserInfo;
import com.passport.domain.LogLoginInfo;
import com.passport.domain.module.UserStatusEnum;
import com.passport.rpc.UserRPCService;
import com.passport.rpc.dto.*;
import com.passport.service.ClientUserInfoService;
import com.passport.service.LogLoginService;
import com.passport.service.constant.MessageConstant;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private LogLoginService logLoginService;

    private final String LOGIN_TOKEN = "passport.login.token.{0}";
    @Value("${app.token.encode.key}")
    private String appTokenEncodeKey;
    @Value("${app.passKey}")
    private String passKey;


    @Override
    public RPCResult<Long> findUserCodeByPin(Long proxyId, String pin) {

        RPCResult<Long> result = new RPCResult<>();
        try {
            ClientUserInfo userInfo = clientUserInfoService.findByPin(proxyId, pin);
            if (userInfo != null) {
                result.setData(userInfo.getId());
                result.setSuccess(true);
                return result;
            }
        } catch (Exception e) {
            logger.error("查询用户失败", e);
        }
        result.setCode("findByUserCodeByPin.error");
        result.setMessage("查询用户code失败");
        return result;
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
    public RPCResult<List<LogLoginDto>> queryLoginLog(LogLoginDto dto) {
        RPCResult<List<LogLoginDto>> result = new RPCResult<>();
        try {
            LogLoginInfo logLoginInfo = new LogLoginInfo();
            BeanCoper.copyProperties(logLoginInfo, dto);
            PageInfo pageinfo = dto.getPageinfo();
            if (pageinfo.getSize() == null) {
                pageinfo.setSize(10);
            }
            Pageable page = pageinfo.getPage();
            Page<LogLoginInfo> pages = logLoginService.queryByPage(logLoginInfo, page);
            List<LogLoginInfo> list = pages.getContent();
            result.setTotalPage(pages.getTotalPages());
            result.setPageSize(page.getPageSize());
            result.setPageIndex(page.getPageNumber());
            result.setTotalCount((int) pages.getTotalElements());

            List<LogLoginDto> dtos = new ArrayList<>();
            for (LogLoginInfo item : list) {
                LogLoginDto dto1 = new LogLoginDto();
                BeanCoper.copyProperties(dto1, item);
                dtos.add(dto1);
            }
            result.setSuccess(true);
            result.setCode("find.LogLoginDto.success");
            result.setMessage("获取成功");
            result.setData(dtos);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode("find.LogLoginDto.error");
            result.setMessage(MessageConstant.FIND_USER_EXTEND_INFO_FAIL);
            logger.error(MessageConstant.FIND_USER_EXTEND_INFO_FAIL, e);
        }
        return result;
    }

    @Override
    public RPCResult<UserDTO> queryUser(Long proxyId, Long userCode) {
        RPCResult<UserDTO> result = new RPCResult<>();
        try {
            if (proxyId == null || userCode == null) {
                result.setSuccess(false);
                result.setCode("param.null");
                return result;
            }

            ClientUserInfo info = new ClientUserInfo();
            info.setProxyId(proxyId);
            info.setId(userCode);
            List<ClientUserInfo> list = clientUserInfoService.query(info);
            if (list == null || list.size() < 1) {
                result.setSuccess(false);
                result.setCode("find.result.null");
                return result;
            }
            UserDTO dto = BeanCoper.copyProperties(UserDTO.class, list.get(0));
            result.setSuccess(true);
            result.setData(dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode("find.LogLoginDto.error");
            result.setMessage(MessageConstant.FIND_USER_EXTEND_INFO_FAIL);
            logger.error(MessageConstant.FIND_USER_EXTEND_INFO_FAIL, e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> addPopUser(Long proxyId, String nickName, String pass) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            PopUserDTO dto = new PopUserDTO();
            String head = String.valueOf(1 + (int) (Math.random() * 20));
            dto.setProxyId(proxyId);
            dto.setNickName(nickName);
            dto.setHeadUrl(head);
            dto.setPasswd(MD5.MD5Str(pass, passKey));
            dto.setSexType(SexEnum.MALE.getValue());
            dto.setStatus(UserStatusEnum.Disable.getValue());
            dto.setBirthDay(new Date());
            dto.setPopularize(YesOrNoEnum.YES.getValue());
            Boolean isTrue = clientUserInfoService.addPopUser(dto);
            result.setSuccess(isTrue);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode("addPopUser.error");
            result.setMessage("添加用户失败");
            logger.error("添加用户失败", e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> resetPass(Long proxyId, Long id,String pass) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            ClientUserInfo clientUserInfo = clientUserInfoService.findById(id);
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

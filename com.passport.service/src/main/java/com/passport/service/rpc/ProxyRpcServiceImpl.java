package com.passport.service.rpc;

import com.alibaba.dubbo.config.annotation.Service;
import com.common.exception.BizException;
import com.common.security.DesDecrypter;
import com.common.security.DesEncrypter;
import com.common.util.BeanCoper;
import com.common.util.PageInfo;
import com.common.util.RPCResult;
import com.common.util.StringUtils;
import com.common.util.model.YesOrNoEnum;
import com.passport.domain.ClientUserInfo;
import com.passport.domain.LimitInfo;
import com.passport.domain.ProxyInfo;
import com.passport.rpc.dto.*;
import com.passport.service.ClientUserInfoService;
import com.passport.service.LimitInfoService;
import com.passport.service.LogLoginService;
import com.passport.rpc.ProxyRpcService;
import com.passport.service.ProxyInfoService;
import com.passport.service.constant.MessageConstant;
import com.passport.service.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service(timeout = 1000)
@org.springframework.stereotype.Service
public class ProxyRpcServiceImpl implements ProxyRpcService {

    private final static Logger logger = Logger.getLogger(ProxyRpcServiceImpl.class);
    @Value("${app.passKey}")
    private String passKey;
    @Value("${app.token.encode.key}")
    private String appTokenEncodeKey;
    @Resource
    private ProxyInfoService proxyInfoService;
    @Resource
    private LogLoginService logLoginService;
    @Resource
    private ClientUserInfoService clientUserInfoService;
    @Resource
    private LimitInfoService limitInfoService;

    private final String loginTokenKey="passport.proxy.token.{0}";
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public RPCResult<List<ProxyDto>> query(ProxyDto dto) {
        RPCResult<List<ProxyDto>> result = new RPCResult<>();
        try{
            PageInfo pageinfo = dto.getPageinfo();
            if(pageinfo == null){
                result.setSuccess(false);
                result.setCode("param.null");
                return null;
            }
            if (pageinfo.getSize() == null) {
                pageinfo.setSize(10);
            }
            Pageable page = pageinfo.getPage();
            ProxyInfo info = new ProxyInfo();
            BeanCoper.copyProperties(info, dto);

            Page<ProxyInfo> pages = proxyInfoService.queryByPage(info, page);
            List<ProxyInfo> list = pages.getContent();
            result.setTotalPage(pages.getTotalPages());
            result.setPageSize(page.getPageSize());
            result.setPageIndex(page.getPageNumber());
            result.setTotalCount((int) pages.getTotalElements());

            List<ProxyDto> dtos = new ArrayList<>();
            for (ProxyInfo item : list) {
                ProxyDto dto1 = new ProxyDto();
                BeanCoper.copyProperties(dto1, item);
                dtos.add(dto1);
            }
            result.setSuccess(true);
            result.setCode("find.ProxyDto.dto.success");
            result.setMessage("获取用户成功");
            result.setData(dtos);
        }catch (Exception e){
            result.setSuccess(false);
            result.setCode("find.ProxyDto.dto.error");
            result.setMessage(MessageConstant.FIND_USER_EXTEND_INFO_FAIL);
            logger.error(MessageConstant.FIND_USER_EXTEND_INFO_FAIL, e);
        }
        return result;
    }

    @Override
    public RPCResult<List<ProxyDto>> queryAll() {
        RPCResult<List<ProxyDto>> result = new RPCResult<>();
        try {
            ProxyInfo entity = new ProxyInfo();
            entity.setStatus(YesOrNoEnum.YES.getValue());
            List<ProxyInfo> queryList = proxyInfoService.query(entity);
            List<ProxyDto> listresult = new ArrayList<>();
            for (ProxyInfo info : queryList) {
                ProxyDto dto = new ProxyDto();
                BeanCoper.copyProperties(dto, info);
                listresult.add(dto);
            }
            result.setData(listresult);
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
    public RPCResult<String> refreshToken(Long proxyId) {
        RPCResult<String> result = new RPCResult();
        try {
            String s = proxyInfoService.refreshToken(proxyId);
            result.setSuccess(true);
            result.setData(s);
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
            String[] arr = new String[]{domain};
            query.setInDomain(arr);
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
            if (proxyInfo == null) {
                result.setSuccess(false);
                result.setCode("proxy.login.error");
                result.setMessage("登陆失败");
                return result;
            }
            ProxyDto dto = new ProxyDto();
            BeanCoper.copyProperties(dto, proxyInfo);
            dto.setAccount(account);
            dto.setToken(proxyInfo.getToken());
            dto.setEncodingKey(proxyInfo.getEncodingKey());
            dto.setLoginToken(StringUtils.getUUID());
            String loginPinToken = MessageFormat.format(loginTokenKey, dto.getLoginToken());
            Object o = redisTemplate.opsForValue().get(loginPinToken);
            if (o != null) {
                redisTemplate.delete(loginPinToken);
            }
            redisTemplate.opsForValue().set(loginPinToken, dto, 1, TimeUnit.DAYS);
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
    public RPCResult logOut(String token) {
        RPCResult rpcResult = new RPCResult<>();
        try {
            token = DesDecrypter.decryptString(token, appTokenEncodeKey);
            token = token.split(":")[2];
            token = MessageFormat.format(loginTokenKey, token);
            redisTemplate.delete(token);
            rpcResult.setSuccess(true);
            return rpcResult;
        } catch (Exception e) {
            rpcResult.setSuccess(false);
            rpcResult.setCode("token.error");
            rpcResult.setMessage("验证登录失效");
        }
        return rpcResult;
    }

    @Override
    public RPCResult<ProxyDto> verfiyToken(String token) {
        RPCResult<ProxyDto> rpcResult = new RPCResult<>();
        try {
            String loginPinToken = MessageFormat.format(loginTokenKey, token);
            ProxyDto o = (ProxyDto) redisTemplate.opsForValue().get(loginPinToken);
            Boolean expire = redisTemplate.expire(loginPinToken, 1, TimeUnit.DAYS);
            if (!expire.booleanValue()) {
                rpcResult.setSuccess(false);
                rpcResult.setCode("token.error");
                rpcResult.setMessage("验证登录失效");
            }
            rpcResult.setData(o);
            rpcResult.setSuccess(true);
            return rpcResult;
        } catch (Exception e) {
            rpcResult.setSuccess(false);
            rpcResult.setCode("token.error");
            rpcResult.setMessage("验证登录失效");
        }
        return rpcResult;
    }

    @Override
    public RPCResult<Boolean> changePass(String account, String oldPass, String newPass) {
        RPCResult<Boolean> result = new RPCResult<>();
        try {
            proxyInfoService.changePass(account, oldPass, newPass);
            result.setSuccess(true);
        }catch (BizException bize){
            result.setSuccess(false);
            result.setCode(bize.getCode());
            result.setMessage(bize.getMessage());
        }catch (Exception e) {
            logger.error("修改密码失败", e);
            result.setSuccess(false);
            result.setCode("proxy.changePass.error");
            result.setMessage("修改密码失败");
        }
        return result;
    }

    @Override
    public RPCResult<Long> queryActiveUsers(Long proxyId, Date startTime, Date endTime) {
        RPCResult<Long> result = new RPCResult<>();
        try {
            if (startTime.getTime() > endTime.getTime()) {
                result.setSuccess(false);
                result.setMessage("时间错误");
                return result;
            }
            if (proxyId == null) {
                result.setSuccess(false);
                result.setMessage("代理错误");
                return result;
            }
            Long count = logLoginService.QueryActiveUsers(proxyId, startTime, endTime);
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
    public RPCResult<Long> queryNewUsers(Long proxyId, Date startTime, Date endTime) {
        RPCResult<Long> result = new RPCResult<>();
        try {
            if (startTime.getTime() > endTime.getTime()) {
                result.setSuccess(false);
                result.setMessage("时间错误");
                return result;
            }

            if (proxyId == null) {
                result.setSuccess(false);
                result.setMessage("代理错误");
                return result;
            }

            Long count = clientUserInfoService.queryCountByRegTime(proxyId, startTime, endTime);
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
    public RPCResult<Long> queryActiveUsers(Long proxyId, DateType type) {
        RPCResult<Long> result = null;
        try {
            Date[] arr = DateUtil.getStartAndEndDate(type);
            result = queryActiveUsers(proxyId, arr[0], arr[1]);
        } catch (Exception e) {
            logger.error("查询活跃人数异常", e);
            result.setSuccess(false);
            result.setCode("query.active.users.error");
            result.setMessage("查询活跃人数异常");
        }
        return result;
    }

    @Override
    public RPCResult<Long> queryNewUsers(Long proxyId, DateType type) {
        RPCResult<Long> result = null;
        try {
            Date[] arr = DateUtil.getStartAndEndDate(type);
            result = queryNewUsers(proxyId, arr[0], arr[1]);
        } catch (Exception e) {
            logger.error("查询新增人数异常", e);
            result.setSuccess(false);
            result.setCode("query.new.users.error");
            result.setMessage("查询新增人数异常");
        }
        return result;
    }

    @Override
    public RPCResult<Double> queryRetention(Long proxyId, Date startTime, Date endTime) {
        RPCResult<Double> result = null;
        try {
            result = new RPCResult<>();
            if (startTime.getTime() > endTime.getTime()) {
                result.setSuccess(false);
                result.setMessage("时间错误");
                return result;
            }

            if (proxyId == null) {
                result.setSuccess(false);
                result.setMessage("代理错误");
                return result;
            }

            int diff = DateUtil.differentDays(startTime, endTime);
            if (diff <= 0) {
                result.setSuccess(false);
                result.setMessage("相隔不足一天，无法计算");
                return result;
            }
            Date endPreOne = DateUtil.setZero(endTime);
            //查询从startTime到endPreOne之间注册的人数
            RPCResult<Long> result_1 = queryNewUsers(proxyId, startTime, endPreOne);
            if (!result_1.getSuccess()) {
                result.setSuccess(false);
                result.setMessage("查询 " + startTime + " 至 " + endPreOne + "之间的新注册人数失败，故无法计算");
                return result;
            }
            Long registerNum = result_1.getData();
            //如果在startTime到endPreOne之间注册的人数为0
            if(registerNum == 0){
                result.setSuccess(false);
                result.setMessage(startTime + " 至 " + endPreOne + "之间的新注册人数为0，故无法计算");
                return result;
            }
            //查询在endTime这一天的登陆人数(即endPreOne到endTime之间)，且注册时间在startTime到endPreOne之间
            Long loginNum = this.logLoginService.QueryLoginUsersByRegDate(proxyId, endPreOne, endTime, startTime, endPreOne);
            //计算
            BigDecimal regBd = BigDecimal.valueOf(registerNum);
            BigDecimal loginBd = BigDecimal.valueOf(loginNum);
            Double res = loginBd.divide(regBd, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
            result.setSuccess(true);
            result.setData(res);
        } catch (Exception e) {
            logger.error("查询留存异常", e);
            result.setSuccess(false);
            result.setCode("query.retention.users.error");
            result.setMessage("查询留存异常");
        }
        return result;
    }

    @Override
    public RPCResult<Double> queryRetention(Long proxyId, DateType type) {
        RPCResult<Double> result = null;
        try {
            Date[] arr = DateUtil.getStartAndEndDate(type);
            result = queryRetention(proxyId, arr[0], arr[1]);
        } catch (Exception e) {
            logger.error("查询留存异常", e);
            result.setSuccess(false);
            result.setCode("query.retention.users.error");
            result.setMessage("查询留存异常");
        }
        return result;
    }

    @Override
    public RPCResult<List<UserDTO>> queryUsersByRegTime(Date startTime, Date endTime,UserDTO dto) {
        RPCResult<List<UserDTO>> result = null;
        try {
            result = new RPCResult<>();
            Long proxyId = dto.getProxyId();
            if (proxyId == null) {
                result.setSuccess(false);
                return result;
            }
            if (startTime.getTime() > endTime.getTime()) {
                result.setSuccess(false);
                return result;
            }
            Page<ClientUserInfo> infos = clientUserInfoService.queryByRegTime(proxyId, startTime, endTime, dto.getPageinfo().getPage());
            List<UserDTO> listResult = new ArrayList<>();
            for (ClientUserInfo clientUserInfo : infos) {
                UserDTO userDTO = new UserDTO();
                BeanCoper.copyProperties(userDTO, clientUserInfo);
                listResult.add(userDTO);
            }
            result.setTotalPage(infos.getTotalPages());
            result.setPageSize(infos.getSize());
            result.setPageIndex(infos.getNumber());
            result.setTotalCount((int) infos.getTotalElements());

            result.setSuccess(true);
            result.setData(listResult);
        } catch (Exception e) {
            logger.error("查询用户列表异常", e);
            result.setSuccess(false);
            result.setCode("query.users.list.error");
            result.setMessage("查询用户列表异常");
        }
        return result;
    }


    @Override
    public RPCResult<UserDTO> queryUsersByCondition(Long proxyId, ConditionType type, String data) {
        RPCResult<UserDTO> result = null;
        try {
            result = new RPCResult<>();
            if (proxyId == null || StringUtils.isBlank(data)) {
                result.setSuccess(false);
                return result;
            }

            ClientUserInfo clientUserInfo = null;
            switch (type) {
                case ID:
                    clientUserInfo = clientUserInfoService.findById(Long.parseLong(data));
                    break;
                case ACCOUNT:
                    clientUserInfo = clientUserInfoService.findByPhone(proxyId, data);
                    break;
                default:
            }

            if (clientUserInfo == null) {
                result.setSuccess(false);
                return result;
            }

            UserDTO userDTO = new UserDTO();
            BeanCoper.copyProperties(userDTO, clientUserInfo);
            result.setSuccess(true);
            result.setData(userDTO);
        } catch (Exception e) {
            logger.error("查询用户信息异常", e);
            result.setSuccess(false);
            result.setCode("query.users.info.error");
            result.setMessage("查询用户信息异常");
        }
        return result;
    }

    @Override
    public RPCResult<List<UserDTO>> queryUsersByNick(UserDTO dto) {
        RPCResult<List<UserDTO>> result = null;
        try {
            result = new RPCResult<>();
            Long proxyId = dto.getProxyId();
            String nick = dto.getNickName();
            if (proxyId == null || StringUtils.isBlank(nick)) {
                result.setSuccess(false);
                return result;
            }
            Page<ClientUserInfo> infos = clientUserInfoService.queryByNick(proxyId, nick, dto.getPageinfo().getPage());
            List<UserDTO> listResult = new ArrayList<>();
            for (ClientUserInfo clientUserInfo : infos) {
                UserDTO userDTO = new UserDTO();
                BeanCoper.copyProperties(userDTO, clientUserInfo);
                listResult.add(userDTO);
            }
            result.setSuccess(true);
            result.setData(listResult);
        } catch (Exception e) {
            logger.error("查询用户列表异常", e);
            result.setSuccess(false);
            result.setCode("query.users.list.error");
            result.setMessage("查询用户列表异常");
        }
        return result;
    }

    @Override
    public RPCResult<ProxyDto> queryUsersSuperior(Long proxyId, String phone) {
        RPCResult<ProxyDto> result = null;
        try {
            result = new RPCResult<>();
            if (proxyId == null || !StringUtils.isMobileNO(phone)) {
                result.setSuccess(false);
                return result;
            }

            ClientUserInfo clientUserInfo = clientUserInfoService.findByPhone(proxyId, phone);
            if (clientUserInfo == null) {
                result.setSuccess(false);
                return result;
            }
            ProxyInfo proxy = proxyInfoService.findById(clientUserInfo.getProxyId());
            if (proxy == null) {
                result.setSuccess(false);
                return result;
            }
            ProxyDto dto = new ProxyDto();
            BeanCoper.copyProperties(dto, proxy);
            result.setSuccess(true);
            result.setData(dto);
        } catch (Exception e) {
            logger.error("查询直属代理信息异常", e);
            result.setSuccess(false);
            result.setCode("query.users.Superior.info.error");
            result.setMessage("查询直属代理信息异常");
        }
        return result;
    }

    @Override
    public RPCResult<ProxyDto> findByPin(Long proxyId, String pin) {
        RPCResult<ProxyDto> result = null;
        try {
            result = new RPCResult<>();
            if (proxyId == null || !StringUtils.isBlank(pin)) {
                result.setSuccess(false);
                return result;
            }
            ProxyInfo proxyInfo = new ProxyInfo();
            proxyInfo.setPin(pin);
            proxyInfo = proxyInfoService.findByOne(proxyInfo);
            if (proxyInfo == null) {
                result.setSuccess(false);
                return result;
            }
            ProxyDto dto = new ProxyDto();
            BeanCoper.copyProperties(dto, proxyInfo);
            result.setSuccess(true);
            result.setData(dto);
        } catch (Exception e) {
            logger.error("查询用户信息异常", e);
            result.setSuccess(false);
            result.setCode("find.users.Superior.info.error");
            result.setMessage("查询用户信息异常");
        }
        return result;
    }


    @Override
    public RPCResult<Boolean> userlimit(LimitDto dto) {
        RPCResult<Boolean> result = null;
        try {
            result = new RPCResult<>();
            String ip = dto.getIp();
            Long proxyId = dto.getProxyId();
            String pin = dto.getPin();
            LimitInfo limitInfo = new LimitInfo();
            if(StringUtils.isNotBlank(ip)){
                limitInfo.setIp(ip);
            }else if(proxyId != null && StringUtils.isNotBlank(pin)){
                limitInfo.setProxyId(proxyId);
                limitInfo.setPin(pin);
            }else{
                result.setSuccess(false);
                result.setCode("param.null");
                return result;
            }
            limitInfo = limitInfoService.findByOne(limitInfo);
            if(limitInfo == null){
                limitInfo = new LimitInfo();
            }
            BeanCoper.copyProperties(limitInfo,dto);
            limitInfoService.save(limitInfo);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode("userlimitIp.error");
            logger.error("", e);
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> setLimitRegisterNum(Integer num) {
        RPCResult<Boolean> result = null;
        try {
            result = new RPCResult<>();
            if(num < 0){
                result.setSuccess(false);
                result.setCode("param.error");
                return result;
            }
            limitInfoService.setLimitRegisterNum(num);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode("ipLimitRegisterNum.error");
            logger.error("", e);
        }
        return result;
    }

    @Override
    public RPCResult<Integer> findAllLimitNum() {
        RPCResult<Integer> result = null;
        try {
            result = new RPCResult<>();
            LimitInfo limitInfo = limitInfoService.findAllLimitNum();
            if(limitInfo == null){
                result.setSuccess(false);
                result.setCode("num.is.null");
                return result;
            }
            result.setSuccess(true);
            result.setData(limitInfo.getAllNum());
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode("findAllLimitNum.error");
            logger.error("", e);
        }
        return result;
    }

    @Override
    public RPCResult<List<LimitDto>> getLimitInfo(Integer min, Integer max,LimitDto dto) {
        RPCResult<List<LimitDto>> result = null;
        try {
            result = new RPCResult<>();
            if(min < 0 || min <max){
                result.setSuccess(false);
                result.setCode("param.error");
                return result;
            }
            PageInfo pageinfo = dto.getPageinfo();
            if(pageinfo == null){
                pageinfo = new PageInfo();
                pageinfo.setSize(10);
                dto.setPageinfo(pageinfo);
            }
            LimitInfo limitInfo = new LimitInfo();
            limitInfo.setStartCurrentIpRegNum(min);
            limitInfo.setEndCurrentIpRegNum(max);

            Page<LimitInfo> infos = limitInfoService.queryByPage(limitInfo, pageinfo.getPage());
            List<LimitDto> listResult = new ArrayList<>();
            for (LimitInfo li : infos) {
                LimitDto limitDto = new LimitDto();
                BeanCoper.copyProperties(limitDto, li);
                listResult.add(limitDto);
            }
            result.setSuccess(true);
            result.setData(listResult);
        } catch (Exception e) {
            logger.error("查询列表异常", e);
            result.setSuccess(false);
            result.setCode("query.list.error");
            result.setMessage("查询列表异常");
        }
        return result;
    }

    @Override
    public RPCResult<List<LimitDto>> getLimitInfo(LimitDto dto) {
        RPCResult<List<LimitDto>> result = null;
        try {
            result = new RPCResult<>();
            PageInfo pageinfo = dto.getPageinfo();
            if(pageinfo == null){
                pageinfo = new PageInfo();
                pageinfo.setSize(10);
                dto.setPageinfo(pageinfo);
            }
            Pageable page = pageinfo.getPage();
            LimitInfo limitInfo = new LimitInfo();
            BeanCoper.copyProperties(limitInfo,dto);

            Page<LimitInfo> pages = limitInfoService.queryByPage(limitInfo, pageinfo.getPage());
            List<LimitInfo> list = pages.getContent();
            result.setTotalPage(pages.getTotalPages());
            result.setPageSize(page.getPageSize());
            result.setPageIndex(page.getPageNumber());
            result.setTotalCount((int) pages.getTotalElements());

            List<LimitDto> listResult = new ArrayList<>();
            for (LimitInfo li : list) {
                LimitDto limitDto = new LimitDto();
                BeanCoper.copyProperties(limitDto, li);
                listResult.add(limitDto);
            }
            result.setSuccess(true);
            result.setData(listResult);
        } catch (Exception e) {
            logger.error("查询列表异常", e);
            result.setSuccess(false);
            result.setCode("query.list.error");
            result.setMessage("查询列表异常");
        }
        return result;
    }

    @Override
    public RPCResult<Long> queryRegNum(Long proxyId,DateType type) {
        RPCResult<Long> result = null;
        try {
            result = new RPCResult<>();
            Date[] arr = DateUtil.getStartAndEndDate(type);
            result = queryRegNum(proxyId, arr[0], arr[1]);
        } catch (Exception e) {
            logger.error("查询列表异常", e);
            result.setSuccess(false);
            result.setCode("query.Reg.error");
            result.setMessage("查询列表异常");
        }
        return result;
    }

    @Override
    public RPCResult<Long> queryRegNum(Long proxyId, Date startDate, Date endDate) {
        RPCResult<Long> result = null;
        try {
            result = new RPCResult<>();
            ClientUserInfo clientUserInfo = new ClientUserInfo();
            clientUserInfo.setProxyId(proxyId);
            clientUserInfo.setStartCreateTime(startDate);
            clientUserInfo.setEndCreateTime(endDate);
            Long res = clientUserInfoService.queryCount(clientUserInfo);
            result.setSuccess(true);
            result.setData(res);
        } catch (Exception e) {
            logger.error("查询列表异常", e);
            result.setSuccess(false);
            result.setCode("query.Reg.error");
            result.setMessage("查询列表异常");
        }
        return result;
    }

    @Override
    public RPCResult<LimitDto> findLimitInfoById(Long id) {
        RPCResult<LimitDto> result = null;
        try {
            result = new RPCResult<>();
            LimitInfo limitInfo = new LimitInfo();
            limitInfo.setId(id);
            limitInfo = limitInfoService.findByOne(limitInfo);
            result.setSuccess(true);
            LimitDto limitDto = new LimitDto();
            BeanCoper.copyProperties(limitDto,limitInfo);
            result.setData(limitDto);
            result.setSuccess(true);
        } catch (Exception e) {
            logger.error("查询列表异常", e);
            result.setSuccess(false);
            result.setCode("query.Reg.error");
            result.setMessage("查询列表异常");
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changeInfo(ProxyDto proxyDto) {
        RPCResult<Boolean> result = null;
        try {
            result = new RPCResult<>();
            Long id = proxyDto.getId();
            if(id == null){
                result.setSuccess(false);
                result.setCode("param.null");
                return result;
            }

            ProxyInfo info = proxyInfoService.findById(id);
            if(info == null){
                result.setSuccess(false);
                result.setCode("ProxyInfo.null");
                return result;
            }

            BeanCoper.copyProperties(info,proxyDto);
            proxyInfoService.save(info);
            result.setSuccess(true);
        } catch (Exception e) {
            logger.error("修改异常", e);
            result.setSuccess(false);
            result.setCode("update.error");
            result.setMessage("修改异常");
        }
        return result;
    }

    @Override
    public RPCResult<Boolean> delLimitInfo(LimitDto dto) {
        RPCResult<Boolean> result = null;
        try {
            result = new RPCResult<>();
            Long id = dto.getId();
            if(id == null){
                result.setSuccess(false);
                result.setCode("param.null");
                return result;
            }
            limitInfoService.delById(id);
            result.setSuccess(true);
        } catch (Exception e) {
            logger.error("修改异常", e);
            result.setSuccess(false);
            result.setCode("update.error");
            result.setMessage("修改异常");
        }
        return result;
    }
}

package com.passport.service.rpc;

import com.alibaba.dubbo.config.annotation.Service;
import com.common.util.BeanCoper;
import com.common.util.RPCResult;
import com.common.util.StringUtils;
import com.common.util.model.YesOrNoEnum;
import com.passport.domain.ClientUserInfo;
import com.passport.domain.ProxyInfo;
import com.passport.rpc.dto.*;
import com.passport.service.ClientUserInfoService;
import com.passport.service.LogLoginService;
import com.passport.rpc.ProxyRpcService;
import com.passport.service.ProxyInfoService;
import com.passport.service.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private ClientUserInfoService clientUserInfoService;

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
    public RPCResult<Long> queryActiveUsers(Long proxyId,Date startTime, Date endTime) {
        RPCResult<Long> result = new RPCResult<>();
        try {
            if (startTime.getTime() > endTime.getTime()) {
                result.setSuccess(false);
                result.setMessage("时间错误");
                return result;
            }

            if(proxyId == null){
                result.setSuccess(false);
                result.setMessage("代理错误");
                return result;
            }

            Long count = logLoginService.QueryActiveUsers(proxyId,startTime, endTime);
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
    public RPCResult<Long> queryNewUsers(Long proxyId,Date startTime, Date endTime) {
        RPCResult<Long> result = new RPCResult<>();
        try {
            if (startTime.getTime() > endTime.getTime()) {
                result.setSuccess(false);
                result.setMessage("时间错误");
                return result;
            }

            if(proxyId == null){
                result.setSuccess(false);
                result.setMessage("代理错误");
                return result;
            }

            Long count = clientUserInfoService.queryCountByRegTime(proxyId,startTime,endTime);
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
    public RPCResult<Long> queryActiveUsers(Long proxyId,DateType type) {
        RPCResult<Long> result = null;
        try{
            Date[] arr = DateUtil.getStartAndEndDate(type);
            result = queryActiveUsers(proxyId,arr[0],arr[1]);
        }catch (Exception e){
            logger.error("查询活跃人数异常", e);
            result.setSuccess(false);
            result.setCode("query.active.users.error");
            result.setMessage("查询活跃人数异常");
        }
        return result;
    }

    @Override
    public RPCResult<Long> queryNewUsers(Long proxyId,DateType type) {
        RPCResult<Long> result = null;
        try{
            Date[] arr = DateUtil.getStartAndEndDate(type);
            result = queryNewUsers(proxyId,arr[0],arr[1]);
        }catch (Exception e){
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
        try{
            if (startTime.getTime() > endTime.getTime()) {
                result.setSuccess(false);
                result.setMessage("时间错误");
                return result;
            }

            if(proxyId == null){
                result.setSuccess(false);
                result.setMessage("代理错误");
                return result;
            }

            int diff = DateUtil.differentDays(startTime,endTime);
            if(diff <= 0){
                result.setSuccess(false);
                result.setMessage("相隔不足一天，无法计算");
                return result;
            }
            Date endPreOne = DateUtil.setZero(endTime);
            //查询从startTime到endPreOne之间注册的人数
            RPCResult<Long> result_1 = queryNewUsers(proxyId, startTime, endPreOne);
            if(!result_1.getSuccess()){
                result.setSuccess(false);
                result.setMessage("查询注册人数失败");
                return result;
            }
            Long registerNum = result_1.getData();
            //查询在endTime这一天的登陆人数(即endPreOne到endTime之间)，且注册时间在startTime到endPreOne之间
            Long loginNum = this.logLoginService.QueryLoginUsersByRegDate(proxyId, endPreOne, endTime, startTime, endPreOne);
            //计算
            BigDecimal regBd = new BigDecimal(registerNum);
            BigDecimal loginBd = new BigDecimal(loginNum);
            Double res = loginBd.divide(regBd,4,BigDecimal.ROUND_HALF_UP).doubleValue();
            result.setSuccess(true);
            result.setData(res);
        }catch (Exception e){
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
        try{
            Date[] arr = DateUtil.getStartAndEndDate(type);
            result = queryRetention(proxyId,arr[0],arr[1]);
        }catch (Exception e){
            logger.error("查询留存异常", e);
            result.setSuccess(false);
            result.setCode("query.retention.users.error");
            result.setMessage("查询留存异常");
        }
        return result;
    }

    @Override
    public RPCResult<Page<UserDTO>> queryUsersByRegTime(Long proxyId, Date startTime, Date endTime,UserDTO dto) {
        RPCResult<Page<UserDTO>> result = null;
        try{
            result = new RPCResult<>();
            if(proxyId == null){
                result.setSuccess(false);
                return result;
            }
            if(startTime.getTime() > endTime.getTime()){
                result.setSuccess(false);
                return result;
            }
            Page<ClientUserInfo> infos = clientUserInfoService.queryByRegTime(proxyId, startTime, endTime, dto.getPageinfo().getPage());
            List<UserDTO> listResult = new ArrayList<>();
            for(ClientUserInfo clientUserInfo:infos){
                UserDTO userDTO = new UserDTO();
                BeanCoper.copyProperties(userDTO,clientUserInfo);
                listResult.add(userDTO);
            }
            Page<UserDTO> users = new PageImpl<>(listResult, dto.getPageinfo().getPage(), dto.getPageinfo().getSize());
            result.setSuccess(true);
            result.setData(users);
        }catch (Exception e){
            logger.error("查询用户列表异常", e);
            result.setSuccess(false);
            result.setCode("query.users.list.error");
            result.setMessage("查询用户列表异常");
        }
        return result;
    }

    @Override
    public RPCResult<Page<UserDTO>> queryUsersByLoginIp(Long proxyId, String ip, UserDTO dto) {
        RPCResult<Page<UserDTO>> result = null;
        try{
            result = new RPCResult<>();
            if(proxyId == null || StringUtils.isBlank(ip)){
                result.setSuccess(false);
                return result;
            }

            Page<ClientUserInfo> logLoginInfos = clientUserInfoService.queryByLastLoginIP(proxyId, ip, dto.getPageinfo().getPage());
            List<UserDTO> listResult = new ArrayList<>();
            for(ClientUserInfo clientUserInfo:logLoginInfos){
                UserDTO userDTO = new UserDTO();
                BeanCoper.copyProperties(userDTO,clientUserInfo);
                listResult.add(userDTO);
            }

            Page<UserDTO> infos = new PageImpl<>(listResult, dto.getPageinfo().getPage(), dto.getPageinfo().getSize());
            result.setSuccess(true);
            result.setData(infos);
        }catch (Exception e){
            logger.error("查询用户列表异常", e);
            result.setSuccess(false);
            result.setCode("query.users.list.error");
            result.setMessage("查询用户列表异常");
        }
        return result;
    }

    @Override
    public RPCResult<Page<UserDTO>> queryUsersByRegIp(Long proxyId, String ip, UserDTO dto) {
        RPCResult<Page<UserDTO>> result = null;
        try{
            result = new RPCResult<>();
            if(proxyId == null || StringUtils.isBlank(ip)){
                result.setSuccess(false);
                return result;
            }

            Page<ClientUserInfo> logLoginInfos = clientUserInfoService.queryByRegIP(proxyId, ip, dto.getPageinfo().getPage());
            List<UserDTO> listResult = new ArrayList<>();
            for(ClientUserInfo clientUserInfo:logLoginInfos){
                UserDTO userDTO = new UserDTO();
                BeanCoper.copyProperties(userDTO,clientUserInfo);
                listResult.add(userDTO);
            }

            Page<UserDTO> infos = new PageImpl<>(listResult, dto.getPageinfo().getPage(), dto.getPageinfo().getSize());
            result.setSuccess(true);
            result.setData(infos);
        }catch (Exception e){
            logger.error("查询用户列表异常", e);
            result.setSuccess(false);
            result.setCode("query.users.list.error");
            result.setMessage("查询用户列表异常");
        }
        return result;
    }

    @Override
    public RPCResult<Page<UserDTO>> queryUsersByLastLoginTime(Long proxyId, Date startTime, Date endTime,UserDTO dto) {
        RPCResult<Page<UserDTO>> result = null;
        try{
            result = new RPCResult<>();
            if(proxyId == null || startTime.getTime() > endTime.getTime()){
                result.setSuccess(false);
                return result;
            }

            Page<ClientUserInfo> logLoginInfos = clientUserInfoService.queryByLastLoginTime(proxyId, startTime, endTime,dto.getPageinfo().getPage());
            List<UserDTO> listResult = new ArrayList<>();
            for(ClientUserInfo clientUserInfo:logLoginInfos){
                UserDTO userDTO = new UserDTO();
                BeanCoper.copyProperties(userDTO,clientUserInfo);
                listResult.add(userDTO);
            }

            Page<UserDTO> infos = new PageImpl<>(listResult, dto.getPageinfo().getPage(), dto.getPageinfo().getSize());
            result.setSuccess(true);
            result.setData(infos);
        }catch (Exception e){
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
        try{
            result = new RPCResult<>();
            if(proxyId == null || StringUtils.isBlank(data)){
                result.setSuccess(false);
                return result;
            }

            ClientUserInfo clientUserInfo = null;
            switch (type){
                case ID:
                    clientUserInfo = clientUserInfoService.findById(Long.parseLong(data));
                    break;
                case ACCOUNT:
                    clientUserInfo = clientUserInfoService.findByPhone(proxyId,data);
                    break;
                    default:
            }

            if(clientUserInfo == null){
                result.setSuccess(false);
                return result;
            }

            UserDTO userDTO = new UserDTO();
            BeanCoper.copyProperties(userDTO,clientUserInfo);
            result.setSuccess(true);
            result.setData(userDTO);
        }catch (Exception e){
            logger.error("查询用户信息异常", e);
            result.setSuccess(false);
            result.setCode("query.users.info.error");
            result.setMessage("查询用户信息异常");
        }
        return result;
    }

    @Override
    public RPCResult<Page<UserDTO>> queryUsersByNick(Long proxyId, String nick,UserDTO dto) {
        RPCResult<Page<UserDTO>> result = null;
        try{
            result = new RPCResult<>();
            if(proxyId == null || StringUtils.isBlank(nick)){
                result.setSuccess(false);
                return result;
            }
            Page<ClientUserInfo> infos = clientUserInfoService.queryByNick(proxyId, nick, dto.getPageinfo().getPage());
            List<UserDTO> listResult = new ArrayList<>();
            for(ClientUserInfo clientUserInfo:infos){
                UserDTO userDTO = new UserDTO();
                BeanCoper.copyProperties(userDTO,clientUserInfo);
                listResult.add(userDTO);
            }
            Page<UserDTO> users = new PageImpl<>(listResult, dto.getPageinfo().getPage(), dto.getPageinfo().getSize());
            result.setSuccess(true);
            result.setData(users);
        }catch (Exception e){
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
        try{
            result = new RPCResult<>();
            if(proxyId == null || !StringUtils.isMobileNO(phone)){
                result.setSuccess(false);
                return result;
            }

            ClientUserInfo clientUserInfo = clientUserInfoService.findByPhone(proxyId,phone);
            if(clientUserInfo == null){
                result.setSuccess(false);
                return result;
            }
            ProxyInfo proxy = proxyInfoService.findById(clientUserInfo.getProxyId());
            if(proxy == null){
                result.setSuccess(false);
                return result;
            }
            ProxyDto dto = new ProxyDto();
            BeanCoper.copyProperties(dto,proxy);
            result.setSuccess(true);
            result.setData(dto);
        }catch (Exception e){
            logger.error("查询直属代理信息异常", e);
            result.setSuccess(false);
            result.setCode("query.users.Superior.info.error");
            result.setMessage("查询直属代理信息异常");
        }
        return result;
    }
}

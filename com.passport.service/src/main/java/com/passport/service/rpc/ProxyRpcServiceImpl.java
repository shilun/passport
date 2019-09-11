package com.passport.service.rpc;

import com.common.rpc.StatusRpcServiceImpl;
import com.common.util.BeanCoper;
import com.common.util.PageInfo;
import com.common.util.RPCResult;
import com.common.util.model.YesOrNoEnum;
import com.passport.domain.ProxyInfo;
import com.passport.rpc.ProxyRpcService;
import com.passport.rpc.dto.ProxyDto;
import com.passport.service.ClientUserInfoService;
import com.passport.service.ProxyInfoService;
import com.passport.service.constant.MessageConstant;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service()
@org.springframework.stereotype.Service
public class ProxyRpcServiceImpl extends StatusRpcServiceImpl implements ProxyRpcService {

    private final static Logger logger = LoggerFactory.getLogger(ProxyRpcServiceImpl.class);
    @Value("${app.passKey}")
    private String passKey;
    @Value("${app.token.encode.key}")
    private String appTokenEncodeKey;
    @Resource
    private ProxyInfoService proxyInfoService;
    @Resource
    private ClientUserInfoService clientUserInfoService;

    private final String loginTokenKey = "passport.proxy.token.{0}";
    private final String LOGIN_PIN = "passport.login.{0}.{1}";
    private final String LOGIN_TOKEN = "passport.login.token.{0}";
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public RPCResult<List<ProxyDto>> query(ProxyDto dto) {
        RPCResult<List<ProxyDto>> result = new RPCResult<>();
        try {
            PageInfo pageinfo = dto.getPageinfo();
            if (pageinfo == null) {
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
        } catch (Exception e) {
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
//
//    @Override
//    public RPCResult<String> refreshToken(Long proxyId) {
//        RPCResult<String> result = new RPCResult();
//        try {
//            String s = proxyInfoService.refreshToken(proxyId);
//            result.setSuccess(true);
//            result.setData(s);
//            return result;
//        } catch (Exception e) {
//            logger.error("刷新代理商token失败", e);
//        }
//        result.setSuccess(false);
//        result.setCode("proxy.refreshToken.error");
//        result.setMessage("刷新代理商token失败");
//        return result;
//    }

    @Override
    public RPCResult<ProxyDto> findById(Long id) {
        RPCResult<ProxyDto> result = new RPCResult<>();
        try {
            ProxyInfo entity = new ProxyInfo();
            entity.setSeqId(id);
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
    public RPCResult<ProxyDto> findByPin(Long proxyId, String pin) {
        RPCResult<ProxyDto> result = new RPCResult();
        result.setSuccess(false);
        result.setCode("method.desable");
        result.setMessage("远程方法已禁用,请使用 findById");
//        try {
//            result = new RPCResult<>();
//            if (proxyId == null || !StringUtils.isBlank(pin)) {
//                result.setSuccess(false);
//                return result;
//            }
//            ProxyInfo proxyInfo = new ProxyInfo();
//            proxyInfo.setPin(pin);
//            proxyInfo = proxyInfoService.findByOne(proxyInfo);
//            if (proxyInfo == null) {
//                result.setSuccess(false);
//                return result;
//            }
//            ProxyDto dto = new ProxyDto();
//            BeanCoper.copyProperties(dto, proxyInfo);
//            result.setSuccess(true);
//            result.setData(dto);
//        } catch (Exception e) {
//            logger.error("查询用户信息异常", e);
//            result.setSuccess(false);
//            result.setCode("find.users.Superior.info.error");
//            result.setMessage("查询用户信息异常");
//        }
        return result;
    }

    @Override
    public RPCResult<Boolean> changeInfo(ProxyDto proxyDto) {
        RPCResult<Boolean> result = null;
        try {
            result = new RPCResult<>();
            String id = proxyDto.getId();
            if (id == null) {
                result.setSuccess(false);
                result.setCode("param.null");
                return result;
            }

            ProxyInfo info = proxyInfoService.findById(id);
            if (info == null) {
                result.setSuccess(false);
                result.setCode("ProxyInfo.null");
                return result;
            }

            BeanCoper.copyProperties(info, proxyDto);
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

}

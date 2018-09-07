package com.passport.service.rpc;

import com.alibaba.dubbo.config.annotation.Service;
import com.common.util.BeanCoper;
import com.common.util.RPCResult;
import com.passport.domain.ProxyInfo;
import com.passport.rpc.ProxyRpcService;
import com.passport.rpc.dto.ProxyDto;
import com.passport.service.ProxyInfoService;
import org.apache.log4j.Logger;

import javax.annotation.Resource;

@Service(timeout = 1000)
@org.springframework.stereotype.Service
public class ProxyRpcServiceImpl implements ProxyRpcService {

    private final static  Logger logger=Logger.getLogger(ProxyRpcServiceImpl.class);
    @Resource
    private ProxyInfoService proxyInfoService;


    @Override
    public RPCResult<ProxyDto> findByDomain(String domain) {
        RPCResult<ProxyDto> result=new RPCResult<>();
        try{
            ProxyInfo query = new ProxyInfo();
            query.setDomain(domain);
            query = proxyInfoService.findByOne(query);
            ProxyDto dto=new ProxyDto();
            BeanCoper.copyProperties(dto,query);
            result.setData(dto);
            result.setSuccess(true);
            return result;
        }
        catch (Exception e){
            logger.error("查找代理商失败",e);
        }
        result.setSuccess(false);
        result.setCode("proxy.find.error");
        result.setMessage("查找代理商失败");
        return result;
    }
}

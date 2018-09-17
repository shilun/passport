package com.passport.web;

import com.common.exception.BizException;
import com.common.util.RPCResult;
import com.common.util.StringUtils;
import com.common.web.AbstractController;
import com.passport.rpc.ProxyRpcService;
import com.passport.rpc.UserRPCService;
import com.passport.rpc.dto.ProxyDto;
import com.passport.rpc.dto.UserDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shilun on 2017/5/12.
 */
public abstract class AbstractClientController extends AbstractController {
    private final Logger LOGGER = Logger.getLogger(AbstractClientController.class);


    @Resource
    private ProxyRpcService proxyRpcService;

    @Value("${app.cookie.encode.key}")
    private String cookieEncodeKey;

    @Resource
    private UserRPCService clientUserInfoService;

    private Map<String, ProxyDto> proxyMap = new HashMap<>();


    protected UserDTO getUserDto() {
        String token = getRequest().getHeader("token");
        if (StringUtils.isBlank(token)) {
            Cookie tokenCookie = null;
            for (Cookie item : getRequest().getCookies()) {
                if (StringUtils.equals(item.getName(), "token")) {
                    tokenCookie = item;
                    break;
                }
            }
            token = tokenCookie.getValue();
        }
        RPCResult<UserDTO> userDTORPCResult = clientUserInfoService.verToken(getDomain().getId(), token);
        if (!userDTORPCResult.getSuccess()) {
            throw new BizException(userDTORPCResult.getCode(), userDTORPCResult.getMessage());
        }
        return userDTORPCResult.getData();
    }

    /**
     * 获取代理商信息
     *
     * @return
     */
    protected ProxyDto getDomain() {
        String domain = StringUtils.getDomain(getRequest().getRequestURL().toString());
        if (proxyMap.get(domain) != null) {
            return proxyMap.get(domain);
        }
        RPCResult<ProxyDto> result = proxyRpcService.findByDomain(domain);
        if (result.getSuccess()) {
            ProxyDto dto = result.getData();
            proxyMap.put(dto.getDomain(), dto);
            return dto;
        }
        throw new BizException("server.domain.error", "服务器域名错误");

    }

    /**
     * 获取IP
     *
     * @return
     */
    protected String getIP() {
        HttpServletRequest request = getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotBlank(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }
}

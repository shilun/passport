package com.passport.main.interceptor;

import com.common.annotation.RoleResource;
import com.common.constants.GlobalContstants;
import com.common.util.RPCResult;
import com.common.util.StringUtils;
import com.passport.rpc.AdminRPCService;
import com.passport.rpc.dto.UserDTO;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class ResourceHandlerInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(ResourceHandlerInterceptor.class);
    @Resource
    private AdminRPCService adminRPCService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            if (method != null && method.getMethodAnnotation(RoleResource.class) != null) {
                RoleResource methodAnnotation = (RoleResource) method.getMethodAnnotation(RoleResource.class);
                if (methodAnnotation == null) {
                    return true;
                }
                List resourceList = (List) request.getSession().getAttribute(GlobalContstants.LOGIN_ROLE_RESOURCE_KEY);
                if (resourceList == null) {
                    String pin = getPin(request, response);
                    if (StringUtils.isBlank(pin)) {
                        return false;
                    }
                    RPCResult<List<String>> roleResource = this.adminRPCService.queryAdminRoles(pin);
                    if (!roleResource.getSuccess().booleanValue()) {
                        logger.error("操作员资源获取失败," + roleResource.getMessage());
                        return false;
                    }
                    resourceList = roleResource.getData();
                    request.getSession().setAttribute(GlobalContstants.LOGIN_ROLE_RESOURCE_KEY, resourceList);
                }
                if (methodAnnotation != null && resourceList.contains(methodAnnotation.resource())) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    protected String getPin(HttpServletRequest request, HttpServletResponse response) {
        Cookie tokenCookie = null;
        String token = request.getHeader("m_token");
        if (StringUtils.isBlank(token)) {
            for (Cookie item : request.getCookies()) {
                if (StringUtils.equals(item.getName(), "m_token")) {
                    tokenCookie = item;
                    break;
                }
            }

            token = tokenCookie.getValue();
        }
        RPCResult<UserDTO> userDTOResult = adminRPCService.verfiyToken(token);
        if (userDTOResult.getSuccess()) {
            return userDTOResult.getData().getPin();
        } else {
            if (tokenCookie != null) {
                tokenCookie.setMaxAge(0);
                response.addCookie(tokenCookie);
            }
        }
        return null;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

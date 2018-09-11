package com.passport.web.interceptor;

import com.common.annotation.RoleResource;
import com.common.constants.GlobalContstants;
import com.common.util.RPCResult;
import com.common.util.StringUtils;
import com.passport.rpc.AdminRPCService;
import com.passport.rpc.dto.UserDTO;
import org.apache.log4j.Logger;
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
    private Logger logger = Logger.getLogger(ResourceHandlerInterceptor.class);
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
                    RPCResult<List<String>> roleResource = this.adminRPCService.queryAdminRoles(getPin(request,response));
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

    protected String getPin(HttpServletRequest request,HttpServletResponse response) {
        Cookie tokenCookie = null;
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            for (Cookie item : request.getCookies()) {
                if (StringUtils.equals(item.getName(), "token")) {
                    tokenCookie = item;
                    break;
                }
            }

            token = tokenCookie.getValue();
        }
        RPCResult<UserDTO> userDTOResult = adminRPCService.verificationToken(token);
        if (userDTOResult.getSuccess()) {
            return userDTOResult.getData().getPin();
        } else {
            if(tokenCookie!=null){
                tokenCookie.setMaxAge(0);
                response.addCookie(tokenCookie);
            }
        }
        return token;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

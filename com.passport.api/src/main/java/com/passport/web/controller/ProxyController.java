package com.passport.web.controller;

import com.passport.service.ClientUserInfoService;
import com.passport.service.ProxyInfoService;
import com.passport.web.AbstractClientController;
import com.passport.web.controller.dto.PageDto;
import com.passport.web.controller.dto.ProxyChangeUserInfoDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Luo
 * @date 2018/9/18 19:05
 */
@Controller
@RequestMapping(value = "/proxy", method = {RequestMethod.POST})
public class ProxyController extends AbstractClientController {
    @Resource
    private ClientUserInfoService loginService;
    @Resource
    private ProxyInfoService proxyInfoService;


    @RequestMapping("changeInfo")
    @ResponseBody
    @ApiOperation(value = "代理修改用户信息")
    public Map<String, Object> changeInfo(@RequestBody ProxyChangeUserInfoDto dto) {
        return buildMessage(() -> {
            if(checkAuth()){
                loginService.proxyChangeUserInfo(getDomain().getSeqId(),dto.getAccount(),dto.getType(),dto.getValue());
            }
            return null;
        });
    }

    @RequestMapping("getUsers")
    @ResponseBody
    @ApiOperation(value = "代理获取名下的用户")
    public Map<String, Object> getUsres(@RequestBody PageDto dto) {
        return buildMessage(() -> {
            if(checkAuth()){
                return loginService.proxyGetUsers(getDomain().getSeqId(),dto.getPageNum());
            }
            return null;
        });
    }


    private Boolean checkAuth(){
        /*String authorization = getRequest().getHeader("Authorization");
        if(StringUtils.isBlank(authorization)){
            return false;
        }
        String domain = StringUtils.getDomain(getRequest().getRequestURL().toString());

        ProxyInfo query=new ProxyInfo();
        query.setDomain(domain);
        ProxyInfo byOne = proxyInfoService.findByOne(query);
        if(byOne == null){
            return false;
        }

        String authkey = MD5.MD5Str(byOne.getToken(), byOne.getEncodingKey());
        if(!authorization.endsWith(authkey)){
            return false;
        }*/
        return true;
    }
}

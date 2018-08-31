package com.passport.web.controller;

import com.common.exception.BizException;
import com.common.util.BeanCoper;
import com.passport.domain.ClientUserInfo;
import com.passport.domain.ProxyInfo;
import com.passport.service.ClientUserInfoService;
import com.passport.service.ProxyInfoService;
import com.passport.service.constant.CodeConstant;
import com.passport.service.constant.MessageConstant;
import com.passport.web.AbstractClientController;
import com.passport.web.controller.dto.ProxyDto;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping(method = {RequestMethod.POST})
public class ProxyInfoController extends AbstractClientController {
    @Resource
    private ProxyInfoService proxyInfoService;
    @Resource
    private ClientUserInfoService userInfoService;

    /**
     * 查询
     *
     * @param info
     * @return
     */
    @RequestMapping("/proxy/list")
    @ResponseBody
    public Map<String, Object> list(@RequestBody ProxyDto info) {
        return buildMessage(() -> {
            ProxyInfo entity = new ProxyInfo();
            BeanCoper.copyProperties(entity, info);
            return proxyInfoService.queryByPage(entity, info.getPageinfo().getPage());
        });
    }

    /**
     * 查询
     *
     * @param content
     * @return
     */
    @RequestMapping("/proxy/view")
    @ResponseBody
    public Map<String, Object> view(@RequestBody String content) {
        return buildMessage(() -> proxyInfoService.findById(getIdByJson(content)));
    }

    /**
     * 保存
     *
     * @param info
     * @return
     */
    @RequestMapping("/proxy/save")
    @ResponseBody
    public Map<String, Object> save(@RequestBody ProxyDto info) {
        return buildMessage(() -> {
            String pin=info.getPin();
            ClientUserInfo userInfo=userInfoService.findByPin(pin);
            if(userInfo==null){
                throw new BizException(CodeConstant.USER_NULL, MessageConstant.USER_NULL);
            }
            ProxyInfo entity = new ProxyInfo();
            BeanCoper.copyProperties(entity, info);
            return proxyInfoService.save(entity);
    });
}
}

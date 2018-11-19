package com.passport.main.controller;

import com.common.exception.ApplicationException;
import com.common.util.IGlossary;
import com.common.util.StringUtils;
import com.common.util.model.SexEnum;
import com.common.util.model.YesOrNoEnum;
import com.passport.domain.ProxyInfo;
import com.passport.domain.module.AgentTypeEnum;
import com.passport.domain.module.VersionTypeEnum;
import com.passport.rpc.dto.BizTypeEnum;
import com.passport.main.AbstractClientController;
import com.passport.service.ProxyInfoService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "公共接口")
@RestController
@RequestMapping(method = {RequestMethod.POST})
public class GlobalController extends AbstractClientController {

    private static Map<String, Class> glosseryItems;

    @Resource
    private ProxyInfoService proxyInfoService;
    static {
        glosseryItems = new HashMap<>();
        glosseryItems.put("yesorno", YesOrNoEnum.class);
        glosseryItems.put("sextype", SexEnum.class);
        glosseryItems.put("games", BizTypeEnum.class);
        glosseryItems.put("agenttype", AgentTypeEnum.class);
        glosseryItems.put("versiontype", VersionTypeEnum.class);
    }

    @RequestMapping(value = "/global/type/{type}")
    @ResponseBody
    public Map<String, Object> buildGlossery(@PathVariable("type") String type) {
        return buildMessage(()->{
                List<Map<String, Object>> keyValues = new ArrayList<Map<String, Object>>();
                if (StringUtils.isBlank(type)) {
                    throw new ApplicationException("buildGlossery Error unKnow type");
                }
                if("proxy".equalsIgnoreCase(type)){
                    List<ProxyInfo> list = proxyInfoService.query(new ProxyInfo());
                    for (ProxyInfo o : list) {
                        ProxyInfo v = o;
                        HashMap<String, Object> item = new HashMap<>();
                        item.put("value", v.getId());
                        item.put("name", v.getName());
                        keyValues.add(item);
                    }
                    return keyValues;
                }
                Class currentEnum = glosseryItems.get(type);
                for (Object o : currentEnum.getEnumConstants()) {
                    IGlossary v = (IGlossary) o;
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("value", v.getValue());
                    item.put("name", v.getName());
                    keyValues.add(item);
                }
                return keyValues;
        });
    }
}

package com.passport.web.controller;

import com.common.exception.ApplicationException;
import com.common.util.IGlossary;
import com.common.util.StringUtils;
import com.common.util.model.SexEnum;
import com.common.util.model.YesOrNoEnum;
import com.common.web.IExecute;
import com.passport.web.AbstractClientController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "公共接口")
@RestController
@RequestMapping(method = {RequestMethod.POST})
public class GlobalController extends AbstractClientController {

    private static Map<String, Class> glosseryItems;

    static {
        glosseryItems = new HashMap<>();
        glosseryItems.put("yesorno", YesOrNoEnum.class);
        glosseryItems.put("sextype", SexEnum.class);
    }

    @RequestMapping(value = "/global/type/{type}")
    @ResponseBody
    public Map<String, Object> buildGlossery(@PathVariable("type") String type) {
        return buildMessage(new IExecute() {
            @Override
            public Object getData() {
                List<Map<String, Object>> keyValues = new ArrayList<Map<String, Object>>();
                if (StringUtils.isBlank(type)) {
                    throw new ApplicationException("buildGlossery Error unKnow type");
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
            }
        });
    }
}

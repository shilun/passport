package com.passport.service.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 此工具类是为了兼容以前的接口返回的数据格式
 * @author Luo
 * @date 2018/9/22 8:57
 */
public class OldPackageMapUtil {
    public static Map<String,Object> toMap(String httpStatusCode,String httpStatusMsg,Boolean success,Map<String,Object> dataMap){
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("success",success);
        resultMap.put("code",httpStatusCode);
        resultMap.put("msg",httpStatusMsg);
        resultMap.put("data",dataMap);
        return resultMap;
    }
    public static Map<String,Object> toFailMap(String httpStatusCode,String httpStatusMsg){
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("success",false);
        resultMap.put("code",httpStatusCode);
        resultMap.put("msg",httpStatusMsg);
        return resultMap;
    }
    public static Map<String,Object> toSuccessMap(String httpStatusCode,String httpStatusMsg){
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("success",true);
        resultMap.put("code",httpStatusCode);
        resultMap.put("msg",httpStatusMsg);
        return resultMap;
    }
}

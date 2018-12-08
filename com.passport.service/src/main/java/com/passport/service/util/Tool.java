package com.passport.service.util;

import com.common.upload.UploadUtil;
import com.mongodb.DBObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;

/**
 * @author Luo
 * @date 2018/9/25 15:04
 */
@Component
public class Tool {
    private static Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
    private Logger logger=Logger.getLogger(Tool.class);
    @Autowired(required = false)
    private UploadUtil uploadUtil;

    /**
     * 将DBObject转为实体
     * @param dbObject
     * @param bean
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public <T> T dbObjectToBean(DBObject dbObject, T bean) throws Exception{
        if (bean == null) {
            return null;
        }
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            String varName = field.getName();
            Object object = dbObject.get(varName);
            if (object != null) {
                BeanUtils.setProperty(bean, varName, object);
            }

        }
        return bean;
    }

    public boolean isNo(String str){
        return pattern.matcher(str).matches();
    }
}

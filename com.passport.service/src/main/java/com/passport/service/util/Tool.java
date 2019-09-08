package com.passport.service.util;

import com.common.upload.UploadUtil;
import com.mongodb.DBObject;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * @author Luo
 * @date 2018/9/25 15:04
 */
@Component
public class Tool {
    private static Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
    private Logger logger=LoggerFactory.getLogger(Tool.class);
    @Autowired(required = false)
    private UploadUtil uploadUtil;

    public boolean isNo(String str){
        return pattern.matcher(str).matches();
    }

    public JSONObject httpGet(URL url) throws Exception{
        HttpURLConnection connection = null;
        InputStream in = null;
        ByteArrayOutputStream baos = null;
        JSONObject jsonObject = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.connect();
            in = connection.getInputStream();
            baos = new ByteArrayOutputStream();
            int i = -1;
            while ((i = in.read()) != -1) {
                baos.write(i);
            }
            //对字符串转码，预防中文乱码
            String jsonStr = new String(baos.toByteArray(), "UTF-8");
            //System.out.println(url.toString() + "-1: " + jsonStr);
            connection.disconnect();

            jsonObject = JSONObject.fromObject(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
            if (baos != null) {
                baos.close();
            }
        }
        return jsonObject;
    }
}

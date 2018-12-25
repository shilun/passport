package com.passport.service.util;

import com.common.exception.ApplicationException;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class MnsUtils {

    @Value("${app.sms.account}")
    private String smsAccount;
    @Value("${app.sms.password}")
    private String smsPassword;
    @Value("${app.sms.url}")
    private String smsUrl;

    public String doSend(String mobole, String content) {
        try {
            HttpPost post = new HttpPost(smsUrl);
            post.setHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("action", "send"));
            nvps.add(new BasicNameValuePair("account", smsAccount));
            nvps.add(new BasicNameValuePair("password", DigestUtils.md5Hex(smsPassword)));
            nvps.add(new BasicNameValuePair("mobile", mobole));
            nvps.add(new BasicNameValuePair("content", content));

            post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            HttpResponse response = SSLClient.createSSLClientDefault().execute(post);
            HttpEntity entity = response.getEntity();
            String returnString = EntityUtils.toString(entity);
            JSONObject jsonObject = JSONObject.fromObject(returnString);
            if (!jsonObject.get("returnstatus").equals("Success")) {
                throw new ApplicationException("发送短信失败" + jsonObject.get("returnstatus"));
            }
            return jsonObject.getString("remainpoint");
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException("发送短信失败", e);
        }
    }
}

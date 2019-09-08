package com.passport.service.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.common.exception.BizException;
import com.common.util.JaxbUtil;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Jeffrey
 */
public class AliyunMnsUtil {
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";
    private final static Logger logger = LoggerFactory.getLogger(AliyunMnsUtil.class);

    private String accessId = null;

    private String accessKey = null;

    private String endpoint = null;

    private String smsTopic = null;

    private static SmsTemplate smsTemplate = null;

    private static boolean initialized = false;

    private Resource templateXml = null;

    private void init() {
        try (InputStream inputStream = templateXml.getInputStream()) {
            String xmlStr = IOUtils.toString(inputStream);
            JaxbUtil jaxbUtil = new JaxbUtil(SmsTemplate.class);
            smsTemplate = jaxbUtil.fromXml(xmlStr);
            initialized = true;
            logger.info("init success!");
        } catch (Exception e) {
            logger.error("init failed!", e);
        }
    }

    public boolean checkSMS(String smsContent) {
        if (!initialized) {
            init();
        }
        boolean templateMatched = false;
        Pattern pattern = null;
        Matcher matcher = null;
        String pureMsg = smsContent.substring(smsContent.indexOf("】") + 1);
        List<SmsTemplate.Template> templateList = smsTemplate.getTemplate();
        for (SmsTemplate.Template template : templateList) {
            pattern = Pattern.compile(template.getRegex());
            matcher = pattern.matcher(pureMsg);
            if (matcher.matches()) {
                templateMatched = true;
                break;
            }
        }
        if (templateMatched) {
            return true;
        }
        return false;
    }

    public void sendSms(Long id, String smsContent, String mobile) throws Exception {
        if (!initialized) {
            init();
        }

        String signName = null;
        String templateCode = null;
        Map<String, String> params = null;

        boolean templateMatched = false;
        Pattern pattern = null;
        Matcher matcher = null;
        String pureMsg = smsContent.substring(smsContent.indexOf("】") + 1);
        List<SmsTemplate.Template> templateList = smsTemplate.getTemplate();
        for (SmsTemplate.Template template : templateList) {
            pattern = Pattern.compile(template.getRegex());
            matcher = pattern.matcher(pureMsg);
            if (matcher.matches()) {
                templateMatched = true;
                signName = template.getSignName();
                templateCode = template.getCode();
                params = new HashMap<String, String>();
                String[] paraKeys = template.getParakeys().split(",");
                for (int i = 0; i < paraKeys.length; i++) {
                    params.put(paraKeys[i], matcher.group(i + 1));
                }
                break;
            }
        }

        if (templateMatched) {
            this.send(id, signName, templateCode, params, mobile);
        } else {
            throw new Exception("No template matched for smsContent=" + smsContent);
        }
    }

    public boolean send(Long bizId, String signName, String templateCode, Map<String, String> params, String mobile) {
        boolean flag = false;
        try {
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessId, accessKey);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            //组装请求对象
            SendSmsRequest request = new SendSmsRequest();
            //使用post提交
            request.setMethod(MethodType.POST);
            //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
            request.setPhoneNumbers(mobile);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(signName);
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(templateCode);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
            request.setTemplateParam(JSONObject.fromObject(params).toString());
            //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");
            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            //请求失败这里会抛ClientException异常
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                logger.info("send sms ok");
                flag = true;
            } else {
                String message = "send sms error with id=" + bizId + " code=" + sendSmsResponse.getCode()
                        + ", message=" + sendSmsResponse.getMessage();
                logger.error(message);
                throw new BizException(message);
            }
        } catch (ClientException e) {
            logger.error("send sms error with id=" + bizId, e);
            throw new BizException("send sms error with id=" + bizId);
        }
        return flag;
    }


    public void setTemplateXml(Resource templateXml) {
        this.templateXml = templateXml;
    }


    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }


    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }


    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }


    public void setSmsTopic(String smsTopic) {
        this.smsTopic = smsTopic;
    }

    /**
     * 生成6位随机验证码
     * @return
     */
    public static String randomSixCode(){
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }
}

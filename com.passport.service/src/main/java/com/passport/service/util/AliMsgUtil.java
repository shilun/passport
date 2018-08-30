package com.passport.service.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * 阿里云短信接口工具类
 */
public class AliMsgUtil {

    private static Logger logger= LoggerFactory.getLogger(AliMsgUtil.class);

    //初始化ascClient需要的几个参数
    private final static String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
    private final static String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
    //替换成你的AK
    private final static String accessKeyId = "LTAI4j3VYmHODO5Q";//你的accessKeyId,参考本文档步骤2
    private final static String accessKeySecret = "zLrpyUSgEgyIQASedbsq1R7xHyuFEN";//你的accessKeySecret，参考本文档步骤2

    private final static String signName = "大三环";//签名
    private final static String templateCode = "SMS_132386341";//模板编号

    /*
     * 将短信发送频率限制在正常的业务流控范围内，默认流控：短信验证码 ：使用同一个签名，对同一个手机号码发送短信验证码，支持1条/分钟，5条/小时 ，累计10条/天。
     */
    public static Boolean sendMsgSingle(String mobile, String code) {
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e1) {
            e1.printStackTrace();
        }
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
        JSONObject param = new JSONObject();
        param.put("product", "dsh");
        param.put("code", code);
        request.setTemplateParam(param.toString());
        SendSmsResponse sendSmsResponse;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            Boolean isSuccess="OK".equals(sendSmsResponse.getCode());
            if(!isSuccess){
                logger.error(sendSmsResponse.getCode()+";"+sendSmsResponse.getMessage());
            }
            return isSuccess;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 生成6位随机验证码
     * @return
     */
    public static String randomSixCode(){
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }

    /**
     * 生成4位随机验证码
     * @return
     */
    public static String randomFourCode(){
        return String.valueOf(new Random().nextInt(8999) + 1000);
    }

}

package com.passport.service.rpc;

import com.common.exception.BizException;
import com.common.util.RPCResult;
import com.passport.domain.SMSInfo;
import com.passport.rpc.SMSInfoRPCService;
import com.passport.service.SMSInfoService;
import com.passport.service.util.AliyunMnsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by shilun on 2017/5/13.
 */

@Service
@com.alibaba.dubbo.config.annotation.Service
public class SMSInfoRPCServiceImpl implements SMSInfoRPCService {

    @Resource
    private SMSInfoService smsInfoService;

    @Autowired(required = false)
    private AliyunMnsUtil aliyunMnsUtil;

    @Override
    public RPCResult<Boolean> buildSMSCode(String mobile, String content, String source) {
//        boolean templateMatched = aliyunMnsUtil.checkSMS(content);
//        if (!templateMatched) {
//            throw new BizException("content.matche.error", "短信内容不匹配模板");
//        }
        RPCResult<Boolean> result = new RPCResult<>();
        result.setData(false);
        try {
            SMSInfo info = new SMSInfo();
            info.setMobile(mobile);
            info.setContent(content);
            info.setSender(source);
            smsInfoService.insert(info);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setCode("send.msg.error");
            result.setMessage("发送短信失败");

        }
        return result;
    }
}

package com.passport.rpc;

import com.common.util.RPCResult;

/**
 * Created by sl on 05/12/16.
 */
public interface SMSInfoRPCService {

    /**
     * 发送短信
     * @param mobile
     * @param content
     * @param source 
     * @param sign 签名
     * @return
     */
    RPCResult<Boolean> buildSMSCode(String mobile,String content, String source,String sign);
}

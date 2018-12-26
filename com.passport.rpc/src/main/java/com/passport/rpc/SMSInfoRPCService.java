package com.passport.rpc;

import com.common.util.RPCResult;

/**
 * Created by shilun on 05/12/16.
 */
public interface SMSInfoRPCService {
    @Deprecated
    /**
     * 发送短信
     * @param mobile
     * @param content
     * @param source
     * @return
     */
     RPCResult<Boolean> buildSMSCode(String mobile,String content, String source);

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

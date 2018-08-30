package com.passport.rpc;

import com.common.util.RPCResult;

/**
 * Created by shilun on 05/12/16.
 */
public interface SMSInfoRPCService {
    /**
     * 发送验证码
     *
     * @param mobile 手机号码
     */
     RPCResult<Boolean> buildSMSCode( String mobile,String content, String source);
}

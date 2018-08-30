package com.passport.rpc;

import com.common.util.RPCResult;

public interface MailRPCService {
    /**
     * @param content base64内容
     * @param source  业务
     * @return
     */
    RPCResult<Boolean> sendMail(String content, String source);
}

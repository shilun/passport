package com.passport.rpc;

import com.passport.rpc.dto.LogLoginDto;

import java.util.Date;

/**
 * @author Luo
 * @date 2018/9/18 15:48
 */
public interface LogLoginService {
    Boolean addLoginLog(String pin,Long proxyId);

    public Long QueryActiveUsers(Date startTime, Date endTime);
}

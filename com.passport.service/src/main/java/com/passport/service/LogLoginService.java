package com.passport.service;

import java.util.Date;

/**
 * @author Luo
 * @date 2018/9/18 15:48
 */
public interface LogLoginService {
    Boolean addLoginLog(String pin,Long proxyId,Date registerDate);

    Long QueryActiveUsers(Long proxyId,Date startTime, Date endTime);

    Long QueryLoginUsersByRegDate(Long proxyId,Date loginStartTime, Date loginEndTime,Date regStartTime, Date regEndTime);
}

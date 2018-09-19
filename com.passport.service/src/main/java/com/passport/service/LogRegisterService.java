package com.passport.service;


import java.util.Date;

/**
 * @author Luo
 * @date 2018/9/18 17:18
 */
public interface LogRegisterService {
    Boolean addRegisterLog(String pin,Long proxyId,Date registerDate);

    Long QueryNewUsers(Long proxyId,Date startTime, Date endTime);
}

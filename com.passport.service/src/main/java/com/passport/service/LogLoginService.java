package com.passport.service;

import com.common.mongo.MongoService;
import com.passport.domain.ClientUserInfo;
import com.passport.domain.LogLoginInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

/**
 * @author Luo
 * @date 2018/9/18 15:48
 */
public interface LogLoginService extends MongoService<LogLoginInfo> {
    Boolean addLoginLog(String pin,Long proxyId,Date registerDate,String ip,Long userCode);

    Long QueryActiveUsers(Long proxyId,Date startTime, Date endTime);

    Long QueryLoginUsersByRegDate(Long proxyId,Date loginStartTime, Date loginEndTime,Date regStartTime, Date regEndTime);

    /**
     * 根据IP查询用户列表
     * @param proxyId
     * @param ip
     * @param pageable
     * @return
     */
    Page<LogLoginInfo> queryByIp(Long proxyId, String ip, Pageable pageable);

    LogLoginInfo getUserLastLoginInfo(Long proxyId, String pin) throws Exception;
}

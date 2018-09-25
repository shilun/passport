package com.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.passport.domain.LogRegisterInfo;
import com.passport.service.LogRegisterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Luo
 * @date 2018/9/22 15:05
 */
@Service
public class LogRegisterServiceImpl extends AbstractMongoService<LogRegisterInfo>  implements LogRegisterService {
    @Override
    protected Class getEntityClass() {
        return LogRegisterInfo.class;
    }

    @Override
    public Page<LogRegisterInfo> queryByRegIP(Long proxyId, String ip, Pageable pageable) {
        LogRegisterInfo info = new LogRegisterInfo();
        info.setProxyId(proxyId);
        info.setIp(ip);
        return queryByPage(info,pageable);
    }

    @Override
    public Page<LogRegisterInfo> queryByRegDate(Long proxyId, Date startDate, Date endDate,Pageable pageable) {
        LogRegisterInfo info = new LogRegisterInfo();
        info.setProxyId(proxyId);
        info.setRegStartTime(startDate);
        info.setRegEndTime(endDate);
        return queryByPage(info,pageable);
    }
}

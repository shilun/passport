package com.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.common.util.StringUtils;
import com.passport.domain.LogLoginInfo;
import com.passport.service.LogLoginService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Luo
 * @date 2018/9/18 15:50
 */
@Service
public class LogLoginServiceImpl extends AbstractMongoService<LogLoginInfo>  implements LogLoginService {
    private static Logger logger = Logger.getLogger(LogLoginServiceImpl.class);

    @Override
    protected Class getEntityClass() {
        return LogLoginInfo.class;
    }

    @Override
    public Boolean addLoginLog(String pin, Long proxyId) {
        Boolean flag = false;
        try{
            if(StringUtils.isBlank(pin) || proxyId == null){
                return false;
            }
            LogLoginInfo info = new LogLoginInfo();
            info.setPin(pin);
            info.setProxyId(proxyId);
            info.setLoginDay(new Date());
            save(info);
            flag = true;
        }catch (Exception e){
            logger.error("",e);
        }
        return flag;
    }

    @Override
    public Long QueryActiveUsers(Long proxyId,Date startTime, Date endTime) {
        try {
            LogLoginInfo info = new LogLoginInfo();
            info.setProxyId(proxyId);
            info.setStartTime(startTime);
            info.setEndTime(endTime);
            return queryCount(info);
        } catch (Exception e) {
            logger.error("",e);
        }
        return null;
    }
}

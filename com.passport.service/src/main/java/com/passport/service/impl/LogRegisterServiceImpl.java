package com.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.common.util.StringUtils;
import com.passport.domain.ClientUserInfo;
import com.passport.domain.LogRegisterInfo;
import com.passport.service.LogRegisterService;
import org.apache.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Luo
 * @date 2018/9/18 17:52
 */
@Service
public class LogRegisterServiceImpl extends AbstractMongoService<LogRegisterInfo> implements LogRegisterService {
    private final static Logger logger=Logger.getLogger(LogRegisterServiceImpl.class);
    @Override
    protected Class getEntityClass() {
        return LogRegisterInfo.class;
    }

    @Override
    public Boolean addRegisterLog(String pin, Long proxyId) {
        Boolean flag = false;
        try{
            if(StringUtils.isBlank(pin) || proxyId == null){
                return false;
            }
            LogRegisterInfo info = new LogRegisterInfo();
            info.setPin(pin);
            info.setProxyId(proxyId);
            info.setRegisterDay(new Date());
            save(info);
            flag = true;
        }catch (Exception e){
            logger.error("",e);
        }
        return flag;
    }

}

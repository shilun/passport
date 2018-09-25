package com.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.common.util.StringUtils;
import com.common.util.model.YesOrNoEnum;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.passport.domain.ClientUserInfo;
import com.passport.domain.LogLoginInfo;
import com.passport.service.LogLoginService;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
    public Boolean addLoginLog(String pin, Long proxyId,Date registerDate,String ip) {
        Boolean flag = false;
        try{
            if(StringUtils.isBlank(pin) || proxyId == null){
                return false;
            }
            LogLoginInfo info = new LogLoginInfo();
            info.setPin(pin);
            info.setProxyId(proxyId);
            info.setLoginDay(new Date());
            info.setRegisterDate(registerDate);
            info.setIp(ip);
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
            info.setLoginStartTime(startTime);
            info.setLoginEndTime(endTime);
            info.setDelStatus(YesOrNoEnum.NO.getValue());

            BasicDBObject[] arr = {
                                    new BasicDBObject("loginDay",new BasicDBObject("$gte",startTime)),
                                    new BasicDBObject("loginDay",new BasicDBObject("$lte",endTime))
                                    };
            DBObject cond = new BasicDBObject("$and",arr);
            DBObject match = new BasicDBObject("$match",cond);
            DBObject groupFields = new BasicDBObject("_id","$pin");
            groupFields.put("count",new BasicDBObject("$sum",1));
            DBObject group = new BasicDBObject("$group",groupFields);
            ((BasicDBObject) match).append("$group",groupFields);

            return this.template.getCollection(info.getClass().getSimpleName()).count(match);
        } catch (Exception e) {
            logger.error("",e);
        }
        return null;
    }

    @Override
    public Long QueryLoginUsersByRegDate(Long proxyId, Date loginStartTime, Date loginEndTime, Date regStartTime, Date regEndTime) {
        LogLoginInfo info = new LogLoginInfo();
        info.setProxyId(proxyId);
        info.setLoginStartTime(loginStartTime);
        info.setLoginEndTime(loginEndTime);
        info.setRegStartTime(regStartTime);
        info.setRegEndTime(regEndTime);
        //TODO  去重
        return queryCount(info);
    }

    @Override
    public Page<LogLoginInfo> queryByIp(Long proxyId, String ip, Pageable pageable) {
        LogLoginInfo info = new LogLoginInfo();
        info.setProxyId(proxyId);
        info.setIp(ip);
        return queryByPage(info,pageable);
    }
}

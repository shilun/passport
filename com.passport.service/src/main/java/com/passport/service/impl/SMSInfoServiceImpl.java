package com.passport.service.impl;

import com.common.exception.BizException;
import com.common.mongo.AbstractMongoService;
import com.common.util.AbstractBaseDao;
import com.common.util.DateUtil;
import com.common.util.DefaultBaseService;
import com.common.util.StringUtils;
import com.common.util.model.YesOrNoEnum;

import com.passport.domain.SMSInfo;
import com.passport.service.SMSInfoService;
import com.passport.service.worker.SMSWorker;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 */
@Service
public class SMSInfoServiceImpl extends AbstractMongoService<SMSInfo> implements SMSInfoService {

    private static final Logger logger = Logger.getLogger(SMSInfoServiceImpl.class);

    @Override
    protected Class getEntityClass() {
        return SMSInfo.class;
    }

    @Resource
    private SMSWorker smsWorker;

    private final String PASS_SEND_COUNT = "passport.send.sms.count.{0}";
    @Resource
    private RedisTemplate redisTemplate;


    @Value("${app.sms.limit.daytotal}")
    private Integer smsDayCount;

    @Override
    public Long insert(SMSInfo entity) {
        String key = MessageFormat.format(PASS_SEND_COUNT, entity.getMobile());
        Date startDate=DateUtil.getStartDate(new Date());
        Date endDate = DateUtil.getEndDate(new Date());
        SMSInfo query=new SMSInfo();
        query.setMobile(entity.getMobile());
        query.setStartCreateTime(startDate);
        query.setEndCreateTime(endDate);
        Integer countTotal = (Integer) redisTemplate.opsForValue().get(key);
        if (countTotal != null) {
            if (countTotal < smsDayCount) {
                incr(key,endDate.getTime()-System.currentTimeMillis());
            } else {
                throw new BizException("passport.sms.day.limit", "短信日限量");
            }
        }
        else{
            incr(key,endDate.getTime()-System.currentTimeMillis());
        }
        super.insert(entity);
        smsWorker.execute(entity);
        return entity.getId();
    }

    /**
     *
     * @param key
     * @param liveTime
     * @return
     */
    public Long incr(String key, long liveTime) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();

        if ((null == increment || increment.longValue() == 0) && liveTime > 0) {//初始设置过期时间
            entityIdCounter.expire(liveTime, TimeUnit.MILLISECONDS);
        }

        return increment;
    }

}

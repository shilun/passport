package com.passport.service.impl;

import com.common.mongo.AbstractMongoService;
import com.common.util.AbstractBaseDao;
import com.common.util.DefaultBaseService;
import com.common.util.StringUtils;
import com.common.util.model.YesOrNoEnum;

import com.passport.domain.SMSInfo;
import com.passport.service.SMSInfoService;
import com.passport.service.worker.SMSWorker;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Random;


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

    @Override
    public Long insert(SMSInfo entity) {
        super.insert(entity) ;
        smsWorker.execute(entity);
        return entity.getId();
    }

}

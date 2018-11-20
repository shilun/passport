package com.passport.service;

import com.common.mongo.MongoService;
import com.passport.domain.SoftWare;
import com.passport.domain.module.AgentTypeEnum;

/**
 * 
 * @desc 软件包 soft_ware
 *
 */
public interface SoftWareService extends MongoService<SoftWare> {
    /**
     * 获取最新包
     * @param type
     * @return
     */
    SoftWare findLastInfo(Long proxyId, AgentTypeEnum type);


}

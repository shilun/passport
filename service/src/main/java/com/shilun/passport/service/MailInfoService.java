package com.shilun.passport.service;

import com.common.mongo.MongoService;
import com.shilun.passport.domain.MailInfo;

/**
 * Created by shilun on 2016/8/15.
 */
public interface MailInfoService extends MongoService<MailInfo> {

    /***
     * 发送邮件成功
     * @param id
     */
    void sendSuccess(Long id);

    /***
     * 发送邮件失更新执行次数
     * @param id
     */
    void  doUpExecCount(Long id, Integer execuCount);
}

package com.passport.service;

import com.passport.domain.LogRegisterInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

/**
 * @author Luo
 * @date 2018/9/22 15:03
 */
public interface LogRegisterService {
    /**
     * 根据注册ip查询用户列表
     * @param proxyId
     * @param ip
     * @return
     */
    Page<LogRegisterInfo> queryByRegIP(Long proxyId, String ip, Pageable pageable);
    /**
     * 根据注册ip查询用户列表
     * @param proxyId
     * @return
     */
    Page<LogRegisterInfo> queryByRegDate(Long proxyId, Date startDate, Date endDate,Pageable pageable);
}

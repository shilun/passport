package com.passport.service;

import com.common.mongo.MongoService;
import com.common.util.model.SexEnum;
import com.passport.domain.ClientUserInfo;
import com.passport.rpc.dto.ProxyDto;
import com.passport.rpc.dto.UserDTO;
import com.passport.service.constant.ChangeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Map;

/**
 * 客户用户接口
 */
public interface ClientUserInfoService extends MongoService<ClientUserInfo> {
    /**
     * 登录
     *
     * @param loginName
     * @param passwd
     * @return
     */
    ClientUserInfo login(String loginName, String passwd);

    /**
     * @param pin
     * @return
     */
    ClientUserInfo findByPin(String pin);

    /**
     * 修改用户密码
     *
     * @param pin
     * @param pwd
     */
    void changePass(String pin, String pwd);


    /***
     * 注册
     * @param
     * @return
     */
    ClientUserInfo regist(String upPin, String phone, String pass);


    /**
     * 密码修改
     *
     * @param pin
     * @param oldPass
     * @param newPass
     * @return
     */
    void changePass(String pin, String oldPass, String newPass);

}

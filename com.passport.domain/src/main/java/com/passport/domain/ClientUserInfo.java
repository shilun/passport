package com.passport.domain;

import com.common.util.AbstractSeqEntity;
import com.common.util.IUserEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 客户用户信息
 */
@Data
@Document(collection = "clientUserInfo")
@CompoundIndexes(
        {
                @CompoundIndex(name = "deviceId", def = "{'deviceUid':1,'proxyId':1}", unique = true)
        })
public class ClientUserInfo extends AbstractSeqEntity implements IUserEntity {
    /**
     * 用户pin
     */
    private String pin;

    /**
     * 第三方账户
     */
    private String refId;

    /**
     * 推荐人
     */
    private String refPin;
    /**
     * 代理商id
     */
    private Long proxyId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 电话
     */
    private String phone;
    /**
     * 邮件
     */
    private String email;
    /**
     * 密码
     */
    private String passwd;
    /**
     * 性别
     */
    private Integer sexType;
    /**
     * 用户状态
     */
    private Integer status;


    private Date birthDay;

    /***
     * 设备唯一id
     */
    private String deviceUid;

    // 头像URL
    private String headUrl;

    //签名
    private String sign;
}

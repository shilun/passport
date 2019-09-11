package com.passport.rpc.dto;

import com.common.util.AbstractDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shilun on 16-12-5.
 */
@Data
public class UserDTO extends AbstractDTO implements Serializable {
    private static final long serialVersionUID = 8642175623513171274L;
    private Long seqId;
    private String email;
    private String pin;
    private String nickName;
    private String phone;
    private String birthday;
    private Integer sexType;
    private String deviceId;
    private String agentType;
    private Boolean initPass;
    private String token;
    private Long proxyId;
    private Date birthDay;
    private String realName;
    private String registerIp;
    private String upPin;

    // 头像URL
    private String headUrl;

    //微信
    private String wechat;
    //身份证号
    private String idCard;
    //qq
    private Long qq;
    /**
     * 用户状态
     */
    private Integer status;
    //二维码图片地址
    private String qrName;

    private Date createTime;
    //是否机器人  1.否  2.是
    private Integer robot;
    //签名
    private String sign;
    /**
     * 是否推广用户 1是  2否
     */
    private Integer popularize;

}

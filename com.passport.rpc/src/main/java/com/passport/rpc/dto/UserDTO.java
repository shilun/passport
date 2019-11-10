package com.passport.rpc.dto;

import com.common.util.AbstractDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shilun on 16-12-5.
 */
@Data
public class UserDTO extends AbstractDTO implements Serializable {
    private static final long serialVersionUID = 8642175623513171274L;
    /**
     * 用户pin(用户电话)
     */
    private String pin;
    /**
     * 上级用户
     */
    private String upPin;
    /**
     * 下级人员列表
     */
    private List<String> pins;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 电话
     */
    private String phone;
    /**
     * 性别
     */
    private Integer sexType;
    /**
     * 用户状态
     */
    private Integer status;
    /**
     * 签名
     */
    private String sign;

    /**
     * 用户token
     */
    private String token;

}

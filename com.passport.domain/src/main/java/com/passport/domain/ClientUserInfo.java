package com.passport.domain;

import com.common.util.AbstractSeqEntity;
import com.common.util.IUserEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * 客户用户信息
 */
@Data
@Document(collection = "clientUserInfo")
@CompoundIndexes(
        {
                @CompoundIndex(name = "uniqueIndex", def = "{'phone':1}", unique = true)
        })
public class ClientUserInfo extends AbstractSeqEntity {
    /**
     * 用户pin(用户电话)
     */
    private String pin;

    /**
     * 上级用户
     */
    private String upPin;
    /**
     * 代理人列表
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
    /**
     * 签名
     */
    private String sign;

    public String getPin() {
        return phone;
    }

    public void setPin(String pin) {
        this.pin = pin;
        this.phone = pin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        this.pin = phone;
    }
}

package com.passport.rpc.dto;

import com.common.util.AbstractDTO;
import org.apache.tomcat.jni.User;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shilun on 16-12-5.
 */
public class PopUserDTO extends UserDTO implements Serializable {
    private static final long serialVersionUID = 8642175623513171274L;

    private String passwd;

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}

package com.passport.web.controller.dto;

import java.io.Serializable;

/**
 *
 */
public class PassDto implements Serializable {
    private String pin;
    private String pass;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}

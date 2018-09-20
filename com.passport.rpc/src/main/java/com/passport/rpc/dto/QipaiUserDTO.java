package com.passport.rpc.dto;

/**
 * @author Luo
 * @date 2018/9/20 13:44
 */
public class QipaiUserDTO {
    private UserDTO userDTO;
    private UserExtendDTO userExtendDTO;

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public UserExtendDTO getUserExtendDTO() {
        return userExtendDTO;
    }

    public void setUserExtendDTO(UserExtendDTO userExtendDTO) {
        this.userExtendDTO = userExtendDTO;
    }
}

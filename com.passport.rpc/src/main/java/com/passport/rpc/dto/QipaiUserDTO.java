package com.passport.rpc.dto;

/**
 * @author Luo
 * @date 2018/9/20 13:44
 */
public class QipaiUserDTO extends UserDTO{
    private UserExtendDTO userExtendDTO;

    public UserExtendDTO getUserExtendDTO() {
        return userExtendDTO;
    }

    public void setUserExtendDTO(UserExtendDTO userExtendDTO) {
        this.userExtendDTO = userExtendDTO;
    }
}

package com.azry.sps.console.shared.dto.users;


import com.azry.sps.common.model.users.SystemUser;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SystemUserDTO implements IsSerializable {

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @GwtIncompatible
    public static SystemUserDTO toDTO(SystemUser systemUser) {
        if (systemUser == null) {
            return null;
        }
        SystemUserDTO dto = new SystemUserDTO();
        dto.setUserName(systemUser.getUsername());
        return dto;
    }

}
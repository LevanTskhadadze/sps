package com.azry.sps.console.shared.users;


import com.azry.sps.console.shared.dto.users.SystemUserDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("servlet/user")
public interface UserService extends RemoteService {

    SystemUserDTO loadAuthorisedUser();
}
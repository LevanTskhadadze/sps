package com.azry.sps.console.shared.users;


import com.azry.sps.console.shared.dto.users.SystemUserDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync {

	void loadAuthorisedUser(AsyncCallback<SystemUserDTO> async);
}
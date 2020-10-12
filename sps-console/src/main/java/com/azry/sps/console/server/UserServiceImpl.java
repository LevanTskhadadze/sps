package com.azry.sps.console.server;


import com.azry.sps.common.model.users.SystemUser;
import com.azry.sps.console.shared.dto.users.SystemUserDTO;
import com.azry.sps.console.shared.users.UserService;
import com.azry.sps.server.services.user.SystemUserManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;

@WebServlet("/sps/servlet/user")
public class UserServiceImpl extends RemoteServiceServlet implements UserService {

	@Inject
	private SystemUserManager systemUserManager;

	@Override
	public SystemUserDTO loadAuthorisedUser() {
		SystemUser systemUser = systemUserManager.loadAuthorisedUser();
		return SystemUserDTO.toDTO(systemUser);
	}
}

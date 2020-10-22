package com.azry.sps.console.server;

import com.azry.sps.console.shared.dto.usergroup.UserGroupDto;
import com.azry.sps.console.shared.usergroup.UserGroupService;
import com.azry.sps.server.services.usergroup.UserGroupManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import java.util.List;

@WebServlet("/sps/servlet/userGroup")
public class UserGroupServiceImpl extends RemoteServiceServlet implements UserGroupService {

	@Inject
	UserGroupManager userGroupManager;

	@Override
	public List<UserGroupDto> getAllUserGroups() {
		return UserGroupDto.toDtos(userGroupManager.getAllUserGroups());
	}
}

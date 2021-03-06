package com.azry.sps.console.server;


import com.azry.sps.common.exception.SPSException;
import com.azry.sps.common.model.users.Permissions;
import com.azry.sps.common.model.users.UserGroup;
import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.usergroup.PermissionsDTO;
import com.azry.sps.console.shared.dto.usergroup.UserGroupDTO;
import com.azry.sps.console.shared.usergroup.UserGroupService;
import com.azry.sps.server.services.usergroup.UserGroupManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import java.util.List;

@WebServlet("sps/servlet/UserGroup")
public class UserGroupServiceImpl extends RemoteServiceServlet implements UserGroupService {

	@Inject
	UserGroupManager userGroupManager;

	@Override
	public List<UserGroupDTO> getUserGroups() {

		return UserGroupDTO.toDTOs(userGroupManager.getUserGroups());
	}

	@Override
	public List<UserGroupDTO> getFilteredUserGroups(String name, PermissionsDTO permission, Boolean isActive) {

		return UserGroupDTO.toDTOs(userGroupManager.getFilteredUserGroups(name,
									permission == null ? null : Permissions.valueOf(permission.name()),
									isActive));
	}

	@Override
	public UserGroupDTO updateUserGroup(UserGroupDTO dto) throws SPSConsoleException {
		try {
			UserGroup userGroup = userGroupManager.updateUserGroup(dto.fromDTO());
			return UserGroupDTO.toDTO(userGroup);
		}
		catch (SPSException ex) {
			throw new SPSConsoleException(ex);
		}
	}

	@Override
	public void deleteUserGroup(long id) throws SPSConsoleException {
		try {
			userGroupManager.deleteUserGroup(id);
		} catch (SPSException ex) {
			throw new SPSConsoleException(ex);
		}
	}

}

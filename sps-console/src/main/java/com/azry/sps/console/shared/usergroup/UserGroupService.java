package com.azry.sps.console.shared.usergroup;

import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.usergroup.PermissionsDTO;
import com.azry.sps.console.shared.dto.usergroup.UserGroupDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;
@RemoteServiceRelativePath("servlet/UserGroup")
public interface UserGroupService extends RemoteService {

	List<UserGroupDTO> getUserGroups();

	List<UserGroupDTO> getFilteredUserGroups(String name, PermissionsDTO permission, Boolean isActive);

	UserGroupDTO updateUserGroup(UserGroupDTO dto) throws SPSConsoleException;

	void deleteUserGroup(long id);
}

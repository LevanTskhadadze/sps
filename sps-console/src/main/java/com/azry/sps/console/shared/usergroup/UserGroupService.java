package com.azry.sps.console.shared.usergroup;

import com.azry.sps.console.shared.dto.usergroup.PermissionsDTO;
import com.azry.sps.console.shared.dto.usergroup.UserGroupDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("servlet/UserGroup")
public interface UserGroupService extends RemoteService {

	List<UserGroupDTO> getUserGroups(String name, PermissionsDTO permission, Boolean isActive);

	UserGroupDTO updateUserGroup(UserGroupDTO dto);

	void deleteUserGroup(long id);
}

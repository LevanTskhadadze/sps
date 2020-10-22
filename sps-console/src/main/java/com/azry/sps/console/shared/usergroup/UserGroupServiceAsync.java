package com.azry.sps.console.shared.usergroup;

import com.azry.sps.console.shared.dto.usergroup.PermissionsDTO;
import com.azry.sps.console.shared.dto.usergroup.UserGroupDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface UserGroupServiceAsync {

	void getUserGroups(String name, PermissionsDTO permission, Boolean isActive, AsyncCallback<List<UserGroupDTO>> async);

	void updateUserGroup(UserGroupDTO dto, AsyncCallback<UserGroupDTO> async);

	void deleteUserGroup(long id, AsyncCallback<Void> async);
}

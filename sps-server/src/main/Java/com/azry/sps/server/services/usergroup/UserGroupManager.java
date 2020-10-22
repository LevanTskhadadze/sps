package com.azry.sps.server.services.usergroup;

import com.azry.sps.common.model.users.Permissions;
import com.azry.sps.common.model.users.UserGroup;

import javax.ejb.Local;
import java.util.List;

@Local
public interface UserGroupManager {

	List<UserGroup> getUserGroups(String name, Permissions permission, Boolean isActive);

	UserGroup updateUserGroup(UserGroup userGroup);

	void deleteUserGroup(Long id);
}

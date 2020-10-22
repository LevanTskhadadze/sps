package com.azry.sps.console.shared.usergroup;

import com.azry.sps.console.shared.dto.usergroup.UserGroupDto;
import com.azry.sps.console.shared.dto.users.SystemUserDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("servlet/userGroup")
public interface UserGroupService extends RemoteService {

	List<UserGroupDto> getAllUserGroups();

}

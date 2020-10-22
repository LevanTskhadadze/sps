package com.azry.sps.console.shared.usergroup;

import com.azry.sps.console.shared.dto.usergroup.UserGroupDto;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface UserGroupServiceAsync {

	void getAllUserGroups(AsyncCallback<List<UserGroupDto>> callback);


}

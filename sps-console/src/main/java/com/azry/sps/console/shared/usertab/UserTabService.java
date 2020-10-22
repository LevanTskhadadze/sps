package com.azry.sps.console.shared.usertab;

import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.users.SystemUserDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import java.util.List;
import java.util.Map;

@RemoteServiceRelativePath("servlet/userTab")
public interface UserTabService extends RemoteService {

	PagingLoadResult<SystemUserDTO> getUsers(int startingIndex, int numberToDisplay, Map<String, String> params);

	void changeActivation(long id);

	SystemUserDTO editParameter(SystemUserDTO dto);

	SystemUserDTO addParameter(SystemUserDTO dto);

	void removeParameter(long id);

}

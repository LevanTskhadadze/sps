package com.azry.sps.console.shared.usertab;

import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.users.SystemUserDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import java.util.List;
import java.util.Map;

public interface UserTabServiceAsync {
	 void getUsers(int startingIndex, int numberToDisplay, Map<String, String> params, AsyncCallback<PagingLoadResult<SystemUserDTO>> callback);

	 void changeActivation(long id, AsyncCallback<Void> callback);

	 void editParameter(SystemUserDTO dto, AsyncCallback<SystemUserDTO> callback);

	 void addParameter(SystemUserDTO dto, AsyncCallback<SystemUserDTO> callback);

	void removeParameter(long id, AsyncCallback<Void> callback);
}

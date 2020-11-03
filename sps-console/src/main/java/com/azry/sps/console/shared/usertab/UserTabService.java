package com.azry.sps.console.shared.usertab;

import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.users.SystemUserDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import java.util.Map;

@RemoteServiceRelativePath("servlet/userTab")
public interface UserTabService extends RemoteService {

	PagingLoadResult<SystemUserDTO> getUsers(int startingIndex, int numberToDisplay, Map<String, String> params);

	void changeActivation(long id);

	SystemUserDTO editParameter(SystemUserDTO dto) throws SPSConsoleException;

	SystemUserDTO addParameter(SystemUserDTO dto) throws SPSConsoleException;

	void removeParameter(long id);

}

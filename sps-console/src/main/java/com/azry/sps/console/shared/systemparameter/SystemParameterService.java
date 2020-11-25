package com.azry.sps.console.shared.systemparameter;

import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDto;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;
import java.util.Map;

@RemoteServiceRelativePath("servlet/sysPar")
public interface SystemParameterService extends RemoteService {
	List<SystemParameterDto> getSystemParameterTab(Map<String, String> params) throws SPSConsoleException;

	void removeParameter(long id);

	void editParameter(SystemParameterDto dto) throws SPSConsoleException;

	SystemParameterDto getParameter(long id);

	SystemParameterDto addParameter(SystemParameterDto dto);


}

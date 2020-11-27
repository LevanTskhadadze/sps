package com.azry.sps.console.shared.systemparameter;

import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;
import java.util.Map;

@RemoteServiceRelativePath("servlet/sysPar")
public interface SystemParameterService extends RemoteService {
	List<SystemParameterDTO> getSystemParameterTab(Map<String, String> params) throws SPSConsoleException;

	void removeParameter(long id);

	void editParameter(SystemParameterDTO dto) throws SPSConsoleException;

	SystemParameterDTO getParameter(long id);

	SystemParameterDTO addParameter(SystemParameterDTO dto);


}

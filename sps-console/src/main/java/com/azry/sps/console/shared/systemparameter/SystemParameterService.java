package com.azry.sps.console.shared.systemparameter;

import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDto;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDtoType;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;
import java.util.Map;

@RemoteServiceRelativePath("servlet/getTab")
public interface SystemParameterService extends RemoteService {
	List<SystemParameterDto> getSystemParameterTab(Map<String, String> params);

	void removeParameter(long id);

	void editParameter(long id, String code, String type, String value, String description);

	SystemParameterDto getParameter(long id);

	SystemParameterDto addParameter(String code, SystemParameterDtoType type, String value, String desc);


}

package com.azry.sps.console.server;

import com.azry.sps.console.server.helper.SystemParameterDtoHelper;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDtoType;
import com.azry.sps.console.shared.systemparameter.SystemParameterService;
import com.azry.sps.server.services.systemparam.SystemParameterManager;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDto;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import java.util.List;
import java.util.Map;

@WebServlet("/sps/servlet/getTab")
public class SystemParameterServiceImpl extends RemoteServiceServlet implements SystemParameterService {

	@Inject
	SystemParameterManager systemParameterManager;


	@Override
	public List<SystemParameterDto> getSystemParameterTab(Map<String, String> params) {
		return SystemParameterDtoHelper.toDTOs(systemParameterManager.getSystemParameters(params));
	}

	@Override
	public void removeParameter(long id) {
		systemParameterManager.remove(id);
	}

	public void editParameter(long id, String code, String type, String value, String description) {
		systemParameterManager.editRow(id, code, type, value, description);
	}

	@Override
	public SystemParameterDto getParameter(long id) {
		return SystemParameterDtoHelper.toDTO(systemParameterManager.getRow(id));
	}

	public SystemParameterDto addParameter(String code, SystemParameterDtoType type, String value, String desc) {
		SystemParameterDto dto = new SystemParameterDto();
		dto.setCode(code);
		dto.setType(type);
		dto.setValue(value);
		dto.setDescription(desc);
		systemParameterManager.addEntry(SystemParameterDtoHelper.toEntity(dto));
		return dto;
	}

}

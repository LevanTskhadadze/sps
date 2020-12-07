package com.azry.sps.console.server;

import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDTO;
import com.azry.sps.console.shared.systemparameter.SystemParameterService;
import com.azry.sps.systemparameters.services.SystemParameterManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import java.util.List;
import java.util.Map;

@WebServlet("/sps/servlet/sysPar")
//@DependsOn("SpIntegrationService")
public class SystemParameterServiceImpl extends RemoteServiceServlet implements SystemParameterService {

	@Inject
	SystemParameterManager systemParameterManager;

//  	@Inject
//	ServiceProviderIntegrationService serviceProviderIntegrationService;

	@Override
	public List<SystemParameterDTO> getSystemParameterTab(Map<String, String> params) throws SPSConsoleException {
/*
		try {
			serviceProviderIntegrationService.pay("aa", 3, "1", new BigDecimal(2.2));
		} catch (SpIntegrationException e) {
			e.printStackTrace();
		}*/
		return SystemParameterDTO.toDTOs(systemParameterManager.getSystemParameters(params));
	}

	@Override
	public void removeParameter(long id) {
		systemParameterManager.remove(id);
	}

	public void editParameter(SystemParameterDTO dto) throws SPSConsoleException {
		systemParameterManager.editRow(SystemParameterDTO.toEntity(dto));
	}

	@Override
	public SystemParameterDTO getParameter(long id) {
		return SystemParameterDTO.toDTO(systemParameterManager.getRow(id));
	}

	public SystemParameterDTO addParameter(SystemParameterDTO dto) {
		dto.setId(systemParameterManager.addEntry(SystemParameterDTO.toEntity(dto)));
		return dto;
	}

}

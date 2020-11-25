package com.azry.sps.console.server;

import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDto;
import com.azry.sps.console.shared.systemparameter.SystemParameterService;
import com.azry.sps.systemparameters.model.services.SystemParameterManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.ejb.DependsOn;
import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import java.util.List;
import java.util.Map;

@WebServlet("/sps/servlet/sysPar")
@DependsOn("sp-integration-service")
public class SystemParameterServiceImpl extends RemoteServiceServlet implements SystemParameterService {

	@Inject
	SystemParameterManager systemParameterManager;

//  	@Inject
//	ServiceProviderIntegrationService serviceProviderIntegrationService;

	@Override
	public List<SystemParameterDto> getSystemParameterTab(Map<String, String> params) {
//		try {
//			serviceProviderIntegrationService.getInfo("gateway1", 0);
//			boolean bb = serviceProviderIntegrationService.pay("gateway1", 0, 0, new BigDecimal("5.5"));
//			bb = true;
//		} catch (SPSException ex) {
//			ex.printStackTrace();
//		}
		return SystemParameterDto.toDTOs(systemParameterManager.getSystemParameters(params));
	}

	@Override
	public void removeParameter(long id) {
		systemParameterManager.remove(id);
	}

	public void editParameter(SystemParameterDto dto) throws SPSConsoleException {
		systemParameterManager.editRow(SystemParameterDto.toEntity(dto));
	}

	@Override
	public SystemParameterDto getParameter(long id) {
		return SystemParameterDto.toDTO(systemParameterManager.getRow(id));
	}

	public SystemParameterDto addParameter(SystemParameterDto dto) {
		dto.setId(systemParameterManager.addEntry(SystemParameterDto.toEntity(dto)));
		return dto;
	}

}

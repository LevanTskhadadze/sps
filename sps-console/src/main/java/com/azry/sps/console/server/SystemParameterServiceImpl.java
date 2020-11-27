package com.azry.sps.console.server;

import com.azry.sps.common.exception.SPSException;
import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDto;
import com.azry.sps.console.shared.systemparameter.SystemParameterService;
import com.azry.sps.integration.sp.ServiceProviderIntegrationServiceImpl;
import com.azry.sps.integration.sp.dto.PayResponse;
import com.azry.sps.systemparameters.model.services.SystemParameterManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.ejb.DependsOn;
import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@WebServlet("/sps/servlet/sysPar")
@DependsOn("SpIntegrationService")
public class SystemParameterServiceImpl extends RemoteServiceServlet implements SystemParameterService {

	@Inject
	SystemParameterManager systemParameterManager;

  	@Inject
	ServiceProviderIntegrationServiceImpl serviceProviderIntegrationServiceImpl;

	@Override
	public List<SystemParameterDto> getSystemParameterTab(Map<String, String> params) throws SPSConsoleException {
		try {
			PayResponse bb = serviceProviderIntegrationServiceImpl.pay("gateway1", 1, "1", new BigDecimal("5.5"));
		} catch (SPSException ex) {
			throw new SPSConsoleException(ex);
		}
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

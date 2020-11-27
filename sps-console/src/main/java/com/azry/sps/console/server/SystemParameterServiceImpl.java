package com.azry.sps.console.server;

import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDTO;
import com.azry.sps.console.shared.systemparameter.SystemParameterService;
import com.azry.sps.systemparameters.model.services.SystemParameterManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.ejb.DependsOn;
import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import java.util.List;
import java.util.Map;

@WebServlet("/sps/servlet/sysPar")
@DependsOn("SpIntegrationService")
public class SystemParameterServiceImpl extends RemoteServiceServlet implements SystemParameterService {

	@Inject
	SystemParameterManager systemParameterManager;

//  	@Inject
//	ServiceProviderIntegrationService serviceProviderIntegrationService;

	@Override
	public List<SystemParameterDTO> getSystemParameterTab(Map<String, String> params) throws SPSConsoleException {
/*		try {
			PayResponse bb = serviceProviderIntegrationService.pay("gateway1", 1, "1", new BigDecimal("5.5"));
		} catch (SPSException ex) {
			throw new SPSConsoleException(ex);
		}
*/		return SystemParameterDTO.toDTOs(systemParameterManager.getSystemParameters(params));
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

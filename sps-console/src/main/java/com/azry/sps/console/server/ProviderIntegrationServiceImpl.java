package com.azry.sps.console.server;

import com.azry.sps.common.exceptions.SPSException;
import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.providerintegration.AbonentInfoDTO;
import com.azry.sps.console.shared.providerintegration.ProviderIntegrationService;
import com.azry.sps.integration.sp.ServiceProviderIntegrationService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;

@WebServlet("sps/servlet/ProviderIntegration")
public class ProviderIntegrationServiceImpl extends RemoteServiceServlet implements ProviderIntegrationService {

	@Inject
	ServiceProviderIntegrationService serviceProviderConnector;

	public AbonentInfoDTO getAbonent(String serviceCode, Long abonentCode) throws SPSConsoleException{
		try {
			return AbonentInfoDTO.toDTO(serviceProviderConnector.getInfo(serviceCode, abonentCode));
		} catch (SPSException ex) {
			throw new SPSConsoleException(ex);
		}
	}

}

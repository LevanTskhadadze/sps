package com.azry.sps.console.server;

import com.azry.sps.common.exception.SPSException;
import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.providerintegration.AbonentInfoDTO;
import com.azry.sps.integration.sp.ProviderIntegrationService;
import com.azry.sps.integration.sp.exception.SpConnectivityException;
import com.azry.sps.console.shared.providerintegration.ProviderIntegrationService;
import com.azry.sps.integration.sp.ServiceProviderIntegrationService;
import com.azry.sps.integration.sp.exception.SpIntegrationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;

@WebServlet("sps/servlet/ProviderIntegration")
public class ProviderIntegrationServiceImpl extends RemoteServiceServlet implements com.azry.sps.console.shared.providerintegration.ProviderIntegrationService {

	@Inject
    ProviderIntegrationService ProviderConnector;

	public AbonentInfoDTO getAbonent(String serviceCode, String abonentCode) throws SPSConsoleException{
		try {
			return AbonentInfoDTO.toDTO(ProviderConnector.getInfo(serviceCode, String.valueOf(abonentCode)));
		} catch (SpConnectivityException ex) {
			throw new SPSConsoleException(new SPSException("spConnectionFailed"));
		}
	}
}

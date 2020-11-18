package com.azry.sps.console.shared.providerintegration;


import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.providerintegration.AbonentInfoDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("servlet/ProviderIntegration")
public interface ProviderIntegrationService extends RemoteService {

	AbonentInfoDTO getAbonent(String serviceCode, Long abonentInfo) throws SPSConsoleException;
}

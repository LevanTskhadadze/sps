package com.azry.sps.console.shared.providerintegration;

import com.azry.sps.console.shared.dto.providerintegration.AbonentInfoDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProviderIntegrationServiceAsync {

	void getAbonent(String serviceCode, Long abonentCode, AsyncCallback<AbonentInfoDTO> async);
}

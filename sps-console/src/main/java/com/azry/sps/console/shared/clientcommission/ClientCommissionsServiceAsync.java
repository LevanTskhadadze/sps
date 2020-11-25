package com.azry.sps.console.shared.clientcommission;

import com.azry.sps.console.shared.dto.commission.clientcommission.ClientCommissionsDto;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

public interface ClientCommissionsServiceAsync {

	void getFilteredClientCommissions(String serviceId, String channelId, PagingLoadConfig config, AsyncCallback<PagingLoadResult<ClientCommissionsDto>> async);

	void getClientCommissionByServiceId(long serviceId, AsyncCallback<ClientCommissionsDto> async);

	void updateClientCommissions(ClientCommissionsDto clientCommissions, AsyncCallback<ClientCommissionsDto> async);

	void deleteClientCommissions(long id, AsyncCallback<Void> async);
}

package com.azry.sps.console.shared.clientcommission;

import com.azry.sps.console.shared.dto.commission.clientcommission.ClientCommissionsDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

public interface ClientCommissionsServiceAsync {

	void getFilteredClientCommissions(String serviceId, String channelId, PagingLoadConfig config, AsyncCallback<PagingLoadResult<ClientCommissionsDTO>> async);

	void getClientCommission(long serviceId, AsyncCallback<ClientCommissionsDTO> async);

	void updateClientCommissions(ClientCommissionsDTO clientCommissions, AsyncCallback<ClientCommissionsDTO> async);

	void deleteClientCommissions(long id, AsyncCallback<Void> async);
}

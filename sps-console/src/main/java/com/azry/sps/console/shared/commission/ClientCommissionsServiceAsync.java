package com.azry.sps.console.shared.commission;

import com.azry.sps.console.shared.dto.commission.ClientCommissionsDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

public interface ClientCommissionsServiceAsync {

	void getFilteredClientCommissions(String serviceId, String channelId, PagingLoadConfig config, AsyncCallback<PagingLoadResult<ClientCommissionsDTO>> async);

	void updateClientCommissions(ClientCommissionsDTO clientCommissions, AsyncCallback<ClientCommissionsDTO> async);

	void deleteClientCommissions(long id, AsyncCallback<Void> async);
}

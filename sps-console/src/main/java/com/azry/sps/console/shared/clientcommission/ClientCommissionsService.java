package com.azry.sps.console.shared.clientcommission;

import com.azry.sps.console.shared.dto.commission.clientcommission.ClientCommissionsDto;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

@RemoteServiceRelativePath("servlet/ClientCommissions")
public interface ClientCommissionsService extends RemoteService {

	PagingLoadResult<ClientCommissionsDto> getFilteredClientCommissions(String serviceID, String channelId, PagingLoadConfig config);

	ClientCommissionsDto updateClientCommissions(ClientCommissionsDto clientCommissions);

	void deleteClientCommissions(long id);
}
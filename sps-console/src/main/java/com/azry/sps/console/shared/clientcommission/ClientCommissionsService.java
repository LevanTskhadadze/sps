package com.azry.sps.console.shared.clientcommission;

import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.commission.clientcommission.ClientCommissionsDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

@RemoteServiceRelativePath("servlet/ClientCommissions")
public interface ClientCommissionsService extends RemoteService {

	PagingLoadResult<ClientCommissionsDTO> getFilteredClientCommissions(String serviceID, String channelId, PagingLoadConfig config);

	ClientCommissionsDTO getClientCommission(long serviceId);

	ClientCommissionsDTO updateClientCommissions(ClientCommissionsDTO clientCommissions) throws SPSConsoleException;

	void deleteClientCommissions(long id);
}

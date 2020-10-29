package com.azry.sps.server.services.commission;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.model.commission.ClientCommissions;

import javax.ejb.Local;

@Local
public interface ClientCommissionsManager {

	ListResult<ClientCommissions> getFilteredClientCommissions(String service, String channel, int offset, int limit);

	ClientCommissions updateClientCommissions(ClientCommissions clientCommissions);

	void deleteClientCommissions(long id);
}

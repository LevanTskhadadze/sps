package com.azry.sps.server.services.clientcommission;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.exception.SPSException;
import com.azry.sps.common.model.commission.ClientCommissions;

import javax.ejb.Local;
import java.math.BigDecimal;

@Local
public interface ClientCommissionsManager {

	ListResult<ClientCommissions> getFilteredClientCommissions(String service, String channel, int offset, int limit);

	ClientCommissions getClientCommissionByServiceId(long serviceId);

	ClientCommissions getClientCommission(long serviceId, long channelId);

	ClientCommissions updateClientCommissions(ClientCommissions clientCommissions) throws SPSException;

	BigDecimal calculateCommission(long serviceId, BigDecimal principal);

	void deleteClientCommissions(long id);
}

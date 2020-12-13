package com.azry.sps.server.services.clientcommission;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.events.UpdateCacheEvent;
import com.azry.sps.common.exception.SPSException;
import com.azry.sps.common.model.commission.ClientCommissions;
import com.azry.sps.common.model.commission.CommissionRateType;
import com.azry.sps.server.caching.CachedConfigurationService;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Stateless
public class ClientCommissionsManagerBean implements ClientCommissionsManager {

	@PersistenceContext
	EntityManager em;

	@Inject
	Event<UpdateCacheEvent> updateCacheEvent;

	@Inject
	CachedConfigurationService cachedConfigurationService;

	@Override
	public ListResult<ClientCommissions> getFilteredClientCommissions(String serviceId, String channelId, int offset, int limit) {
		return cachedConfigurationService.filterClientCommissions(serviceId, channelId, offset, limit);
	}

	public ClientCommissions getClientCommissionByServiceId(long serviceId){
		return cachedConfigurationService.getClientCommissionByServiceId(String.valueOf(serviceId));
	}

	@Override
	public ClientCommissions getClientCommission(long serviceId, long channelId) {
		return cachedConfigurationService.getClientCommission(String.valueOf(serviceId), String.valueOf(channelId));
	}

	@Override
	public ClientCommissions updateClientCommissions(ClientCommissions clientCommissions) throws SPSException {
		try {
			clientCommissions = em.merge(clientCommissions);
			UpdateCache();
			return clientCommissions;
		}
		catch (OptimisticLockException ex) {
			throw new SPSException("optimisticLockException", ex);
		}
	}


	@Override
	public void deleteClientCommissions(long id) {
		ClientCommissions clientCommissions = em.find(ClientCommissions.class, id);
		if (clientCommissions != null) {
			em.remove(clientCommissions);
			UpdateCache();
		}
	}

	public BigDecimal calculateCommission(long serviceId, BigDecimal principal) {
		BigDecimal commission;
		ClientCommissions clientCommissions = cachedConfigurationService.getClientCommissionByServiceId(String.valueOf(serviceId));
		if (clientCommissions == null) {
			return null;
		}
		if (clientCommissions.getRateType().equals(CommissionRateType.FIXED)) {
			commission = clientCommissions.getCommission();
		} else {
			commission = principal.multiply(clientCommissions.getCommission()).divide(new BigDecimal(100));
			commission = commission.setScale(2, RoundingMode.UP);
		}
		if (clientCommissions.getMinCommission() != null
			&& commission.compareTo(clientCommissions.getMinCommission()) < 0) {
			commission = clientCommissions.getMinCommission();
		} else if (clientCommissions.getMaxCommission() != null
			&& commission.compareTo(clientCommissions.getMaxCommission()) > 0) {
			commission = clientCommissions.getMaxCommission();
		}
		return commission;
	}

	private void UpdateCache() {
		updateCacheEvent.fire(new UpdateCacheEvent(ClientCommissions.class.getSimpleName()));
	}
}

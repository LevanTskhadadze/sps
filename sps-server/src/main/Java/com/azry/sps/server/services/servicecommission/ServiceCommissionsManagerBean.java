package com.azry.sps.server.services.servicecommission;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.events.UpdateCacheEvent;
import com.azry.sps.common.exception.SPSException;
import com.azry.sps.common.model.commission.ServiceCommissions;
import com.azry.sps.server.caching.CachedConfigurationService;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;

@Stateless
public class ServiceCommissionsManagerBean implements ServiceCommissionsManager {

	@PersistenceContext
	EntityManager em;

	@Inject
	private CachedConfigurationService cachingService;

	@Inject
	Event<UpdateCacheEvent> updateCacheEvent;

	@Override
	public ListResult<ServiceCommissions> getFilteredServiceCommissions(String serviceId, int offset, int limit) {

		return cachingService.getFilteredServiceCommissions(serviceId, offset, limit);

	}

	@Override
	public ServiceCommissions updateServiceCommissions(ServiceCommissions serviceCommissions) throws SPSException {
		try {
			ServiceCommissions commission = em.merge(serviceCommissions);
			updateCache();
			return commission;

		}
		catch (OptimisticLockException ex) {
			throw new SPSException("optimisticLockException", ex);
		}
	}

	@Override
	public void deleteServiceCommissions(long id) {
		ServiceCommissions serviceCommissions = em.find(ServiceCommissions.class, id);
		if (serviceCommissions != null) {
			em.remove(serviceCommissions);
			updateCache();
		}
	}

	@Override
	public ServiceCommissions getCommissionForService(long serviceId) {
		return cachingService.getCommissionForService(String.valueOf(serviceId));
	}

	private void updateCache() {
		updateCacheEvent.fire(new UpdateCacheEvent(ServiceCommissions.class.getSimpleName()));
	}
}

package com.azry.sps.server.caching.servicecommissions;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.model.commission.ServiceCommissions;
import com.azry.sps.server.caching.CachingService;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServiceCommissionsCachingManager implements CachingService<ServiceCommissions, Long> {

	private final EntityManager em;

	private final Logger log;

	private final Map<Long, ServiceCommissions> cachedServiceCommissions = new HashMap<>();

	private Date lastSyncDate = new Date();

	public ServiceCommissionsCachingManager(EntityManager em, Logger log) {
		this.em = em;
		this.log = log;
		loadCache();
	}

	@Override
	public ServiceCommissions get(Long key) {
		return cachedServiceCommissions.get(key);
	}

	@Override
	public List<ServiceCommissions> getList() {
		List<ServiceCommissions> serviceCommissions = new ArrayList<>(cachedServiceCommissions.values());
		serviceCommissions.sort((o1, o2) -> (int) (o1.getId() - o2.getId()));
		return serviceCommissions;
	}

	@Override
	public void syncData() {
		syncUpdatedData();
		syncDeletedData();
	}

	public ListResult<ServiceCommissions> getFilteredServiceCommissions(String serviceId, int offset, int limit) {
		List<ServiceCommissions> filteredServiceCommissions = getList();

		for (ServiceCommissions serviceCommission : new ArrayList<>(filteredServiceCommissions)) {
			if (serviceCommission.getServicesIds() != null && serviceId != null) {
				List<String> serviceIds = Arrays.asList(serviceCommission.getServicesIds().split(","));
				if (!serviceCommission.isAllServices() && !serviceIds.contains(serviceId)) {
					filteredServiceCommissions.remove(serviceCommission);
				}
			}
		}

		if (offset >= filteredServiceCommissions.size()) {
			offset = 0;
		}
		int lastIndex = Math.min(offset + limit, filteredServiceCommissions.size());

		return new ListResult<>(new ArrayList<>(filteredServiceCommissions.subList(offset, lastIndex)), filteredServiceCommissions.size());
	}



	private void loadCache() {
		List<ServiceCommissions> serviceCommissions = getAllServiceCommissions();
		updateCache(serviceCommissions);
	}

	private List<ServiceCommissions> getAllServiceCommissions() {
		return em.createQuery("SELECT s FROM ServiceCommissions s", ServiceCommissions.class)
			.getResultList();
	}

	private void updateCache(List<ServiceCommissions> serviceCommissions) {
		for (ServiceCommissions commission : serviceCommissions) {
			cachedServiceCommissions.put(commission.getId(), commission);
			em.detach(commission);
		}
	}

	private void syncUpdatedData() {
		String q = "SELECT s FROM ServiceCommissions s WHERE s.lastUpdateTime IS NOT NULL AND s.lastUpdateTime > :lastSyncDate ORDER BY s.lastUpdateTime DESC";
		List<ServiceCommissions> updatedEntities = em.createQuery(q, ServiceCommissions.class)
			.setParameter("lastSyncDate", lastSyncDate)
			.getResultList();
		updateCache(updatedEntities);
		if (!updatedEntities.isEmpty()) {
			lastSyncDate = updatedEntities.get(0).getLastUpdateTime();
		}
	}


	private void syncDeletedData() {
		Set<Long> ids = new HashSet<>(em.createQuery("SELECT c.id FROM ClientCommissions c", Long.class)
			.getResultList());
		Set<Long> toBeDeletedIds = new HashSet<>(cachedServiceCommissions.keySet());
		toBeDeletedIds.removeAll(ids);
		for (Long id : toBeDeletedIds) {
			cachedServiceCommissions.remove(id);
		}
	}
}

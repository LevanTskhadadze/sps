package com.azry.sps.server.caching.clientcommissions;


import com.azry.sps.common.ListResult;
import com.azry.sps.common.model.commission.ClientCommissions;
import com.azry.sps.server.caching.CachingService;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClientCommissionsCachingManager implements CachingService<ClientCommissions, Long> {

	@PersistenceContext
	private final EntityManager em;

	private final Logger log;

	private final Map<Long, ClientCommissions> cachedClientCommissions = new HashMap<>();

	private Date lastSyncDate = new Date();

	public ClientCommissionsCachingManager(EntityManager em, Logger log) {
		this.em = em;
		this.log = log;
		loadCache();
	}

	@Override
	public ClientCommissions get(Long key) {
		return cachedClientCommissions.get(key);
	}

	@Override
	public List<ClientCommissions> getList() {
		List<ClientCommissions> clientCommissions = new ArrayList<>(cachedClientCommissions.values());
		clientCommissions.sort((o1, o2) -> (int) (o1.getId() - o2.getId()));
		return clientCommissions;
	}

	@Override
	public void syncData() {
		syncUpdatedData();
		syncDeletedData();
	}

	public ListResult<ClientCommissions> filterClientCommissions(String serviceId, String channelId, int offset, int limit) {
		List<ClientCommissions> filteredClientCommissions = getList();

		for (ClientCommissions clientCommissions : new ArrayList<>(filteredClientCommissions)) {
			if (clientCommissions.getServicesIds() != null && serviceId != null) {
				List<String> serviceIds = Arrays.asList(clientCommissions.getServicesIds().split(","));
				if (!clientCommissions.isAllServices() && !serviceIds.contains(serviceId)) {
					filteredClientCommissions.remove(clientCommissions);
				}
			}

			if (clientCommissions.getChannelsIds() != null && channelId != null) {
				List<String> channelIds = Arrays.asList(clientCommissions.getChannelsIds().split(","));
				if (!clientCommissions.isAllChannels() && !channelIds.contains(channelId)) {
					filteredClientCommissions.remove(clientCommissions);
				}
			}
		}

		if (offset >= filteredClientCommissions.size()) {
			offset = 0;
		}
		int lastIndex = Math.min(offset + limit, filteredClientCommissions.size());

		return new ListResult<>(new ArrayList<>(filteredClientCommissions.subList(offset, lastIndex)), filteredClientCommissions.size());
	}



	private void loadCache() {
		List<ClientCommissions> clientCommissions = getAllClientCommissions();
		updateCache(clientCommissions);
	}

	private List<ClientCommissions> getAllClientCommissions() {
		return em.createQuery("SELECT c FROM ClientCommissions c", ClientCommissions.class)
			.getResultList();
	}

	private void updateCache(List<ClientCommissions> clientCommissions) {
		for (ClientCommissions commission : clientCommissions) {
			cachedClientCommissions.put(commission.getId(), commission);
			em.detach(commission);
		}
	}

	private void syncUpdatedData() {
		String ql = "SELECT c FROM ClientCommissions c WHERE c.lastUpdateTime IS NOT NULL AND c.lastUpdateTime > :lastSyncDate ORDER BY c.lastUpdateTime DESC";
		List<ClientCommissions> updatedEntities = em.createQuery(ql, ClientCommissions.class)
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
		Set<Long> toBeDeletedIds = new HashSet<>(cachedClientCommissions.keySet());
		toBeDeletedIds.removeAll(ids);
		for (Long id : toBeDeletedIds) {
			cachedClientCommissions.remove(id);
		}
	}
}
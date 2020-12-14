package com.azry.sps.server.caching.services;


import com.azry.sps.common.ListResult;
import com.azry.sps.common.model.service.Service;
import com.azry.sps.common.model.service.ServiceChannelInfo;
import com.azry.sps.common.model.service.ServiceEntity;
import com.azry.sps.server.caching.CachingService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServicesCachingManager implements CachingService<Service, Long> {

	@PersistenceContext
	private final EntityManager em;

	private final Logger log;

	private final Map<Long, Service> cachedServices = new HashMap<>();

	private Date lastSyncDate = new Date();

	public ServicesCachingManager(EntityManager em, Logger log) {
		this.em = em;
		this.log = log;
		loadCache();
	}

	@Override
	public Service get(Long key) {
		return cachedServices.get(key);
	}

	@Override
	public List<Service> getList() {
		List<Service> services = new ArrayList<>(cachedServices.values());
		services.sort((o1, o2) -> (o1.getName().compareToIgnoreCase(o2.getName())));
		return services;
	}

	@Override
	public void syncData() {
		syncUpdatedData();
		syncDeletedData();
	}

	public List<Service> getAllServices() {
		return getList();
	}

	public ListResult<Service> filterServices(Map<String, String> filter, int offset, int limit) {
		List<Service> filteredServices = getList();

		for (Service service : new ArrayList<>(filteredServices)) {
			if (service.getName() != null && filter.get("name") != null) {
				String name = filter.get("name");
				if (StringUtils.isEmpty(service.getName()) || !service.getName().toLowerCase().contains(name.toLowerCase())) {
					filteredServices.remove(service);
					continue;
				}
			}

			if (filter.get("active") != null
						&& service.isActive() != filter.get("active").equals("1")) {
				filteredServices.remove(service);
			}
		}


		if (offset >= filteredServices.size()) {
			offset = 0;
		}
		int lastIndex = Math.min(offset + limit, filteredServices.size());

		return new ListResult<>(new ArrayList<>(filteredServices.subList(offset, lastIndex)), filteredServices.size());
	}

	public List<Service> getAllActiveServices() {
		List<Service> activeServices = getList();

		activeServices.removeIf(service -> !service.isActive());
		return activeServices;
	}

	public List<Service> getServicesByServiceGroup(Long groupId) {
		if (groupId == null) {
			return null;
		}
		List<Service> filteredServices = getList();

		filteredServices.removeIf(service -> !groupId.equals(service.getGroupId()) || !service.isActive());
		return filteredServices;
	}

	public Service getServiceByServicePayCode(String serviceCode) {
		if (serviceCode == null) {
			return null;
		}
		List<Service> filteredServices = getList();

		for (Service service : filteredServices) {
			if (serviceCode.equals(service.getServicePayCode()) && service.isActive()) {
				return service;
			}
		}
		return null;
	}

	private boolean channelIsActive(Service service, long channelId) {
		for (ServiceChannelInfo channelInfo : service.getChannels()) {
			if (channelInfo.getChannelId() == channelId) {
				return channelInfo.isActive();
			}
		}
		return false;
	}

	private void loadCache() {
		List<Service> services = loadAllServices();
		updateCache(services);
	}

	private List<Service> loadAllServices() {
		return getServices(em.createQuery("SELECT s FROM ServiceEntity s", ServiceEntity.class)
			.getResultList());
	}

	private void updateCache(List<Service> services) {
		for (Service service : services) {
			cachedServices.put(service.getId(), service);
			em.detach(service.getEntity());
		}
	}

	private void syncUpdatedData() {
		String ql = "SELECT s FROM ServiceEntity s WHERE s.lastUpdateTime IS NOT NULL AND s.lastUpdateTime > :lastSyncDate ORDER BY s.lastUpdateTime DESC";
		List<ServiceEntity> updatedEntities = em.createQuery(ql, ServiceEntity.class)
			.setParameter("lastSyncDate", lastSyncDate)
			.getResultList();
		updateCache(getServices(updatedEntities));
		if (!updatedEntities.isEmpty()) {
			lastSyncDate = updatedEntities.get(0).getLastUpdateTime();
		}
	}

	private List<Service> getServices(List<ServiceEntity> entities) {
		List<Service> services = new ArrayList<>();
		for (ServiceEntity entity : entities) {
			services.add(entity.getService());
		}
		return services;
	}

	private void syncDeletedData() {
		Set<Long> ids = new HashSet<>(em.createQuery("SELECT s.id FROM ServiceEntity s", Long.class)
			.getResultList());
		Set<Long> toBeDeletedIds = new HashSet<>(cachedServices.keySet());
		toBeDeletedIds.removeAll(ids);
		for (Long id : toBeDeletedIds) {
			cachedServices.remove(id);
		}
	}

	public List<Service> getServicesByChannelId(Long channelId) {
		List<Service> allServices = getAllServices();
		List<Service> services = new ArrayList<>();
		for (Service service : allServices) {
			if (service.isAllChannels()) {
				services.add(service);
				continue;
			}

			for (ServiceChannelInfo info : service.getChannels()) {
				if (info.getChannelId() == channelId) {
					if (info.isActive()) {
						services.add(service);
					}
					break;
				}
			}
		}

		return services;
	}
}
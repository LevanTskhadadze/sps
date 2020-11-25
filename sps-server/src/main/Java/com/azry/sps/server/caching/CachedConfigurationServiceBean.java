package com.azry.sps.server.caching;


import com.azry.sps.common.ListResult;
import com.azry.sps.common.events.UpdateCacheEvent;
import com.azry.sps.common.model.caching.CachedDataType;
import com.azry.sps.common.model.channels.Channel;
import com.azry.sps.common.model.commission.ClientCommissions;
import com.azry.sps.common.model.commission.ServiceCommissions;
import com.azry.sps.common.model.service.Service;
import com.azry.sps.server.caching.channel.ChannelCachingManger;
import com.azry.sps.server.caching.clientcommissions.ClientCommissionsCachingManager;
import com.azry.sps.server.caching.servicecommissions.ServiceCommissionsCachingManager;
import com.azry.sps.server.caching.services.ServicesCachingManager;
import com.azry.sps.systemparameters.model.SystemParameterType;
import com.azry.sps.systemparameters.model.sysparam.Parameter;
import com.azry.sps.systemparameters.model.sysparam.SysParam;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Startup
@Singleton(name = "CachedConfigurationServiceBean")
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Slf4j
public class CachedConfigurationServiceBean implements CachedConfigurationService {

	private final static String SERVICE_NAME = "Configuration Caching Service";

	@Resource
	private TimerService timerService;

	@PersistenceContext
	private EntityManager em;

	@Inject @SysParam(type = SystemParameterType.INTEGER, code = "cacheSyncInterval", defaultValue = "600")
	private Parameter<Integer> cacheSyncInterval;

	private final Map<String, CachingService> cacheMap = new HashMap<>();

	private boolean initialized;

	@PostConstruct
	public void startup() {
		log.info("Starting " + SERVICE_NAME);
		registerCachingManagers();
		timerService.createIntervalTimer(0, cacheSyncInterval.getValue() * 1000, new TimerConfig(null, false));
	}

	private void registerCachingManagers() {
		cacheMap.put(CachedDataType.SERVICES.getClassSimpleName(), new ServicesCachingManager(em, log));
		cacheMap.put(CachedDataType.CLIENT_COMMISSIONS.getClassSimpleName(), new ClientCommissionsCachingManager(em, log));
		cacheMap.put(CachedDataType.CHANNEL.getClassSimpleName(), new ChannelCachingManger(em, log));
		cacheMap.put(CachedDataType.SERVICE_COMMISSIONS.getClassSimpleName(), new ServiceCommissionsCachingManager(em, log));
		initialized = true;
	}

	@Timeout
	public void sync() {
		log.info("Executing cached configuration synchronization");
		for (CachedDataType type : CachedDataType.values()) {
			cacheMap.get(type.getClassSimpleName()).syncData();
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void sync(@Observes(during = TransactionPhase.AFTER_SUCCESS) UpdateCacheEvent evt) {
		CachingService cachingService = cacheMap.get(evt.getConfClass());
		if (cachingService != null) {
			log.info(String.format("Starting %s cached data synchronization...", evt.getConfClass()));
			cachingService.syncData();
			log.info(String.format("Finished %s cached data synchronization...", evt.getConfClass()));
		}
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}

	@Override
	public Service getService(long serviceId) {
		return ((ServicesCachingManager)cacheMap.get(CachedDataType.SERVICES.getClassSimpleName())).get(serviceId);
	}

	@Override
	public List<Service> getAllServices() {
		return ((ServicesCachingManager)cacheMap.get(CachedDataType.SERVICES.getClassSimpleName())).getList();
	}

	@Override
	public List<Service> getAllActiveServices() {
//		Map<String, String> mp = new HashMap<>();
//		mp.put(ServiceColumnNames.ACTIVE.getName(), ServiceColumnNames.ActivationStatus.ACTIVE.getStatus());
//		return ((ServicesCachingManager)cacheMap.get(CachedDataType.SERVICES.getClassSimpleName())).filterServices(mp, 0, 1000000).getResultList();
		return ((ServicesCachingManager) cacheMap.get(CachedDataType.SERVICES.getClassSimpleName())).getAllActiveServices();
	}

	@Override
	public ListResult<Service> filterServices(Map<String, String> filter, int offset, int limit) {
		return ((ServicesCachingManager) cacheMap.get(CachedDataType.SERVICES.getClassSimpleName())).filterServices(filter, offset, limit);
	}

	@Override
	public List<Service> getServicesByServiceGroup(Long groupId) {
		return ((ServicesCachingManager) cacheMap.get(CachedDataType.SERVICES.getClassSimpleName())).getServicesByServiceGroup(groupId);
	}

	@Override
	public ClientCommissions getClientCommission(long clientCommission) {
		return ((ClientCommissionsCachingManager)cacheMap.get(CachedDataType.CLIENT_COMMISSIONS.getClassSimpleName())).get(clientCommission);
	}

	@Override
	public List<ClientCommissions> getAllClientCommissions() {
		return ((ClientCommissionsCachingManager)cacheMap.get(CachedDataType.CLIENT_COMMISSIONS.getClassSimpleName())).getList();
	}


	@Override
	public ListResult<ClientCommissions> filterClientCommissions(String serviceId, String channelId, int offset, int limit) {
		return ((ClientCommissionsCachingManager) cacheMap.get(CachedDataType.CLIENT_COMMISSIONS.getClassSimpleName()))
			.filterClientCommissions(serviceId, channelId, offset, limit);
	}

	@Override
	public ClientCommissions getClientCommissionByServiceId(String serviceId) {
		return ((ClientCommissionsCachingManager)cacheMap.get(CachedDataType.CLIENT_COMMISSIONS.getClassSimpleName()))
			.getClientCommissionByServiceId(serviceId);
	}

	@Override
	public List<Channel> getChannels() {
		return ((ChannelCachingManger) cacheMap.get(CachedDataType.CHANNEL.getClassSimpleName())).getChannels();
	}

	@Override
	public List<Channel> getFilteredChannels(String name, Boolean isActive) {
		return ((ChannelCachingManger) cacheMap.get(CachedDataType.CHANNEL.getClassSimpleName())).getFilteredChannels(name, isActive);
	}

	@Override
	public ListResult<ServiceCommissions> getFilteredServiceCommissions(String serviceId, int offset, int limit) {
		return ((ServiceCommissionsCachingManager) cacheMap.get(CachedDataType.SERVICE_COMMISSIONS.getClassSimpleName()))
			.getFilteredServiceCommissions(serviceId, offset, limit);
	}
}
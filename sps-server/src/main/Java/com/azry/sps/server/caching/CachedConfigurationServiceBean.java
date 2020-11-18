package com.azry.sps.server.caching;


import com.azry.sps.common.ListResult;
import com.azry.sps.common.events.UpdateCacheEvent;
import com.azry.sps.common.model.caching.CachedDataType;
import com.azry.sps.common.model.commission.ClientCommissions;
import com.azry.sps.common.model.service.Service;
import com.azry.sps.common.model.service.ServiceColumnNames;
import com.azry.sps.server.caching.clientcommissions.ClientCommissionsCachingManager;
import com.azry.sps.server.caching.services.ServicesCachingManager;
import com.azry.sps.systemparameters.model.SystemParameterType;
import com.azry.sps.systemparameters.model.sysparam.Parameter;
import com.azry.sps.systemparameters.model.sysparam.SysParam;

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
public class CachedConfigurationServiceBean implements CachedConfigurationService {

	private final static String SERVICE_NAME = "Configuration Caching Service";

/*	@Inject
	private Logger log;
*/
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
		//log.info("Starting " + SERVICE_NAME);
		registerCachingManagers();
		timerService.createIntervalTimer(0, cacheSyncInterval.getValue() * 1000, new TimerConfig(null, false));
	}

	private void registerCachingManagers() {
		cacheMap.put(CachedDataType.SERVICES.getClassSimpleName(), new ServicesCachingManager(em, null));
		cacheMap.put(CachedDataType.CLIENT_COMMISSIONS.getClassSimpleName(), new ClientCommissionsCachingManager(em, null));
		initialized = true;
	}

	@Timeout
	public void sync() {
		//log.info("Executing cached configuration synchronization");
		for (CachedDataType type : CachedDataType.values()) {
			cacheMap.get(type.getClassSimpleName()).syncData();
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void sync(@Observes(during = TransactionPhase.AFTER_SUCCESS) UpdateCacheEvent evt) {
		CachingService cachingService = cacheMap.get(evt.getConfClass());
		if (cachingService != null) {
//			log.info(String.format("Starting %s cached data synchronization...", evt.getConfClass()));
			cachingService.syncData();
//			log.info(String.format("Finished %s cached data synchronization...", evt.getConfClass()));
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
		Map<String, String> mp = new HashMap<>();
		mp.put(ServiceColumnNames.ACTIVE.getName(), ServiceColumnNames.ActivationStatus.ACTIVE.getStatus());
		return ((ServicesCachingManager)cacheMap.get(CachedDataType.SERVICES.getClassSimpleName())).filterServices(mp, 0, 1000000).getResultList();
	}

	@Override
	public ListResult<Service> filterServices(Map<String, String> filter, int offset, int limit) {
		return ((ServicesCachingManager) cacheMap.get(CachedDataType.SERVICES.getClassSimpleName())).filterServices(filter, offset, limit);
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
		return ((ClientCommissionsCachingManager) cacheMap.get(CachedDataType.CLIENT_COMMISSIONS.getClassSimpleName())).filterClientCommissions(serviceId, channelId, offset, limit);
	}

/*
	@Override
	public List<ServiceProvider> getAllServiceProviders() {
		return ((ServiceProvidersCachingManager)cacheMap.get(CachedDataType.SERVICE_PROVIDERS.getClassSimpleName())).getList();
	}

	@Override
	public ServiceProvider getServiceProvider(long serviceProviderId) {
		return ((ServiceProvidersCachingManager)cacheMap.get(CachedDataType.SERVICE_PROVIDERS.getClassSimpleName())).get(serviceProviderId);
	}

	@Override
	public Channel getChannel(long channelId) {
		return ((ChannelsCachingManager)cacheMap.get(CachedDataType.CHANNELS.getClassSimpleName())).get(channelId);
	}

	@Override
	public List<Channel> getAllChannels() {
		return ((ChannelsCachingManager)cacheMap.get(CachedDataType.CHANNELS.getClassSimpleName())).getList();
	}

	@Override
	public List<GroupCommissions> getAllGroupCommissions() {
		return ((GroupCommissionsCachingManager)cacheMap.get(CachedDataType.GROUP_COMMISSIONS.getClassSimpleName())).getList();
	}

	@Override
	public List<AccountsInfo> getAccountsInfo() {
		return ((AccountsInfoCachingManager)cacheMap.get(CachedDataType.ACCOUNTS_INFOS.getClassSimpleName())).getList();
	}

	@Override
	public PointOfSale getPointOfSale(long pointOfSaleId) {
		return ((PointOfSalesCachingManager)cacheMap.get(CachedDataType.POINT_OF_SALES.getClassSimpleName())).get(pointOfSaleId);
	}

	@Override
	public Region getRegion(Long regionId) {
		return ((RegionsCachingManager)cacheMap.get(CachedDataType.REGIONS.getClassSimpleName())).get(regionId);
	}

	@Override
	public ServiceGroup getServiceGroup(Long serviceGroupId) {
		return ((ServiceGroupCachingManager)cacheMap.get(CachedDataType.SERVICE_GROUPS.getClassSimpleName())).get(serviceGroupId);
	}

	@Override
	public Agent getAgent(String code) {
		return ((AgentsCachingManager)cacheMap.get(CachedDataType.AGENTS.getClassSimpleName())).get(code);
	}

	@Override
	public List<Agent> getAllAgents() {
		return ((AgentsCachingManager)cacheMap.get(CachedDataType.AGENTS.getClassSimpleName())).getList();
	}

	@Override
	public List<Agent> getAgents(List<String> codes) {
		List<Agent> agents = new ArrayList<>();
		for (String code : codes) {
			agents.add(getAgent(code));
		}
		return agents;
	}

	@Override
	public List<PointOfSale> getPointOfSales() {
		return ((PointOfSalesCachingManager)cacheMap.get(CachedDataType.POINT_OF_SALES.getClassSimpleName())).getList();
	}

	@Override
	public List<Region> getAllRegions() {
		return ((RegionsCachingManager)cacheMap.get(CachedDataType.REGIONS.getClassSimpleName())).getList();
	}

	@Override
	public ListResult<Region> filterRegions(RegionsFilter filter) {
		return ((RegionsCachingManager)cacheMap.get(CachedDataType.REGIONS.getClassSimpleName())).filterRegions(filter);
	}

	@Override
	public List<ServiceGroup> getAllServiceGroups() {
		return ((ServiceGroupCachingManager)cacheMap.get(CachedDataType.SERVICE_GROUPS.getClassSimpleName())).getList();
	}

	@Override
	public ListResult<ServiceGroup> filterServiceGroups(ServiceGroupsFilter filter) {
		return ((ServiceGroupCachingManager)cacheMap.get(CachedDataType.SERVICE_GROUPS.getClassSimpleName())).filterServiceGroups(filter);
	}

	public ListResult<Channel> filterChannels(ChannelsFilter filter) {
		return ((ChannelsCachingManager) cacheMap.get(CachedDataType.CHANNELS.getClassSimpleName())).filterChannels(filter);
	}

	@Override
	public ListResult<ServiceProvider> filterServiceProviders(ServiceProvidersFilter filter) {
		return ((ServiceProvidersCachingManager) cacheMap.get(CachedDataType.SERVICE_PROVIDERS.getClassSimpleName())).filterServiceProviders(filter);
	}

	@Override
	public ListResult<Offer> filterOffers(OffersFilter offersFilter) {
		return ((OfferCachingManager) cacheMap.get(CachedDataType.OFFER.getClassSimpleName())).filterOffers(offersFilter);
	}

	@Override
	public ListResult<Agent> filterAgents(AgentsFilter filter) {
		return ((AgentsCachingManager) cacheMap.get(CachedDataType.AGENTS.getClassSimpleName())).filterAgents(filter);
	}


	@Override
	public ListResult<PointOfSale> filterPointOfSales(PointOfSalesFilter filter) {
		return ((PointOfSalesCachingManager)cacheMap.get(CachedDataType.POINT_OF_SALES.getClassSimpleName())).filterPointOfSales(filter);
	}

	@Override
	public ListResult<AccountsInfo> filterAccountsInfos(AccountsInfosFilter filter) {
		return ((AccountsInfoCachingManager)cacheMap.get(CachedDataType.ACCOUNTS_INFOS.getClassSimpleName())).filterAccountsInfos(filter);
	}

	@Override
	public List<Offer> getAllOffers() {
		return ((OfferCachingManager) cacheMap.get(CachedDataType.OFFER.getClassSimpleName())).getList();
	}

	@Override
	public Offer getOffer(long serviceId) {
		return ((OfferCachingManager) cacheMap.get(CachedDataType.OFFER.getClassSimpleName())).get(serviceId);
	}

	@Override
	public PaymentAutomaticManualActionConfiguration getPaymentAutomaticManualActionConfiguration(long id) {
		return ((PaymentAutomaticManualActionConfigurationCachingManager) cacheMap.get(CachedDataType.PAYMENT_AUTO_MANUAL_ACTION_CONFIG.getClassSimpleName())).get(id);
	}

	@Override
	public List<PaymentAutomaticManualActionConfiguration> getAllPaymentAutomaticManualActionConfigurations() {
		return ((PaymentAutomaticManualActionConfigurationCachingManager) cacheMap.get(CachedDataType.PAYMENT_AUTO_MANUAL_ACTION_CONFIG.getClassSimpleName())).getList();
	}*/
}
package com.azry.sps.server.caching;



import com.azry.sps.common.ListResult;
import com.azry.sps.common.events.UpdateCacheEvent;
import com.azry.sps.common.model.commission.ClientCommissions;
import com.azry.sps.common.model.service.Service;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;

@Local
public interface CachedConfigurationService {

	void sync();

	void sync(UpdateCacheEvent evt);

	boolean isInitialized();

	Service getService(long serviceId);

    ClientCommissions getClientCommission(long clientCommission);

    List<Service> getAllServices();

	List<Service> getAllActiveServices();

	ListResult<Service> filterServices(Map<String, String> filter, int offset, int limit);

	List<ClientCommissions> getAllClientCommissions();

	ListResult<ClientCommissions> filterClientCommissions(String serviceId, String channelId, int offset, int limit);

/*
	List<ServiceProvider> getAllServiceProviders();

	ServiceProvider getServiceProvider(long serviceProviderId);

	Channel getChannel(long channelId);

	List<Channel> getAllChannels();

	List<GroupCommissions> getAllGroupCommissions();

	List<AccountsInfo> getAccountsInfo();

	PointOfSale getPointOfSale(long pointOfSaleId);

	Region getRegion(Long regionId);

	ServiceGroup getServiceGroup(Long serviceGroupId);

	Agent getAgent(String code);

	List<Agent> getAllAgents();

	List<Agent> getAgents(List<String> codes);

	List<PointOfSale> getPointOfSales();

	List<Region> getAllRegions();

	ListResult<Region> filterRegions(RegionsFilter filter);

	List<ServiceGroup> getAllServiceGroups();

	ListResult<ServiceGroup> filterServiceGroups(ServiceGroupsFilter filter);

	ListResult<Channel> filterChannels(ChannelsFilter filter);

	ListResult<ServiceProvider> filterServiceProviders(ServiceProvidersFilter filter);

	ListResult<Offer> filterOffers(OffersFilter offersFilter);

	ListResult<Agent> filterAgents(AgentsFilter filter);

	ListResult<PointOfSale> filterPointOfSales(PointOfSalesFilter filter);

	ListResult<AccountsInfo> filterAccountsInfos(AccountsInfosFilter filter);

	List<Offer> getAllOffers();

	Offer getOffer(long serviceId);

	PaymentAutomaticManualActionConfiguration getPaymentAutomaticManualActionConfiguration(long id);

	List<PaymentAutomaticManualActionConfiguration> getAllPaymentAutomaticManualActionConfigurations();
*/
}
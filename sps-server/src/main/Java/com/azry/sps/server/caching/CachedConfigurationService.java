package com.azry.sps.server.caching;


import com.azry.sps.common.ListResult;
import com.azry.sps.common.events.UpdateCacheEvent;
import com.azry.sps.common.model.channels.Channel;
import com.azry.sps.common.model.commission.ClientCommissions;
import com.azry.sps.common.model.commission.ServiceCommissions;
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

	List<Service> getServicesByServiceGroup(Long groupId);

	ListResult<Service> filterServices(Map<String, String> filter, int offset, int limit);

	List<ClientCommissions> getAllClientCommissions();

	ListResult<ClientCommissions> filterClientCommissions(String serviceId, String channelId, int offset, int limit);

	ClientCommissions getClientCommissionByServiceId(String serviceId);

	List<Channel> getChannels();

	List<Channel> getFilteredChannels(String name, Boolean isActive);

	ListResult<ServiceCommissions> getFilteredServiceCommissions(String serviceId, int offset, int limit);

}
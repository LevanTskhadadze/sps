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

    List<Service> getAllServices();

	List<Service> getAllActiveServices();

	List<Service> getServicesByServiceGroup(Long groupId);

	Service getServiceByPayCode(String servicePayCode);

	ListResult<Service> filterServices(Map<String, String> filter, int offset, int limit);

	List<ClientCommissions> getAllClientCommissions();

	ClientCommissions getClientCommission(long clientCommission);

	ListResult<ClientCommissions> filterClientCommissions(String serviceId, String channelId, int offset, int limit);

	ClientCommissions getClientCommissionByServiceId(String serviceId);

	ClientCommissions getClientCommission(String serviceId, String channelId);

	List<Channel> getAllChannels();

	Channel getChannel(long channelId);

	List<Channel> getFilteredChannels(String name, Boolean isActive);

	List<ServiceCommissions> getAllServiceCommissions();

	ListResult<ServiceCommissions> getFilteredServiceCommissions(String serviceId, int offset, int limit);

	ServiceCommissions getCommissionForService(String serviceId);

    List<Service> getServicesByChannelId(Long channelId);
}
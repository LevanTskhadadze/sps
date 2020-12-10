package com.azry.sps.server.services.channel;

import com.azry.sps.common.events.UpdateCacheEvent;
import com.azry.sps.common.exception.SPSException;
import com.azry.sps.common.model.channels.Channel;
import com.azry.sps.common.model.commission.ClientCommissions;
import com.azry.sps.server.caching.CachedConfigurationService;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Stateless
public class ChannelManagerBean implements ChannelManager {

	@PersistenceContext
	private EntityManager em;

	@Inject
	private CachedConfigurationService cachingService;

	@Inject
	Event<UpdateCacheEvent> updateCacheEvent;

	@Override
	public List<Channel> getChannels() {
		return cachingService.getAllChannels();
	}

	@Override
	public Channel getChannel(long id) {
		return cachingService.getChannel(id);
	}

	@Override
	public List<Channel> getFilteredChannels(String name, Boolean isActive) {
		return cachingService.getFilteredChannels(name, isActive);
	}

	@Override
	public Channel updateChannel(Channel channel) throws SPSException {
		try {
			Channel tempChannel = em.merge(channel);
			updateCache();
			return tempChannel;
		}
		catch (OptimisticLockException ex) {
			throw new SPSException("optimisticLockException", ex);
		}
	}

	@Override
	public void deleteChannel(long id) throws SPSException {
		for (ClientCommissions clientCommission : cachingService.getAllClientCommissions()) {
			List<String> channelIds = new ArrayList<>(Arrays.asList(clientCommission.getChannelsIds().split(",")));
			channelIds.removeIf(channelId -> channelId.equals(String.valueOf(id)));
				clientCommission.setChannelsIds(String.join(",", channelIds));
				em.merge(clientCommission);
			}
		updateCacheEvent.fire(new UpdateCacheEvent(ClientCommissions.class.getSimpleName()));

		Channel channel = em.find(Channel.class, id);
		if (channel != null) {
			em.remove(channel);
			updateCache();
		}
	}

	private void updateCache() {
		updateCacheEvent.fire(new UpdateCacheEvent(Channel.class.getSimpleName()));
	}
}

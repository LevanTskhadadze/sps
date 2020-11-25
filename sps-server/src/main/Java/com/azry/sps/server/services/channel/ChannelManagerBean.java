package com.azry.sps.server.services.channel;

import com.azry.sps.common.events.UpdateCacheEvent;
import com.azry.sps.common.exception.SPSException;
import com.azry.sps.common.model.channels.Channel;
import com.azry.sps.server.caching.CachedConfigurationService;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
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

		return cachingService.getChannels();

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
	public void deleteChannel(long id) {
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

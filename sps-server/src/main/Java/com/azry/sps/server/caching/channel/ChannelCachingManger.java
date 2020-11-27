package com.azry.sps.server.caching.channel;

import com.azry.sps.common.model.channels.Channel;
import com.azry.sps.server.caching.CachingService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChannelCachingManger implements CachingService<Channel, Long> {

	private final EntityManager em;

	private final Logger log;

	private final Map<Long, Channel> cachedChannels = new HashMap<>();

	private Date lastSyncDate = new Date();

	public ChannelCachingManger(EntityManager em, Logger log) {
		this.em = em;
		this.log = log;
		loadCache();
	}

	@Override
	public Channel get(Long key) {
		return cachedChannels.get(key);
	}

	@Override
	public List<Channel> getList() {
		List<Channel> channels = new ArrayList<>(cachedChannels.values());
		channels.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
		return channels;
	}

	@Override
	public void syncData() {
		syncUpdatedData();
		syncDeletedData();
	}

	public List<Channel> getChannels() {
		return getList();
	}

	public List<Channel> getFilteredChannels(String name, Boolean isActive) {
		List<Channel> filteredChannels = getList();

		for (Channel channel : new ArrayList<>(filteredChannels)) {
			if (!StringUtils.isEmpty(name) && !channel.getName().toLowerCase().contains(name.toLowerCase())) {
				filteredChannels.remove(channel);
				continue;
			}

			if (isActive != null && channel.isActive() != isActive) {
				filteredChannels.remove(channel);
			}
		}

		return filteredChannels;
	}



	private void loadCache() {
		List<Channel> channels = getAllChannels();
		updateCache(channels);
	}

	private List<Channel> getAllChannels() {
		return em.createQuery("SELECT c FROM Channel c", Channel.class)
			.getResultList();
	}

	private void updateCache(List<Channel> channels) {
		for (Channel channel: channels) {
			cachedChannels.put(channel.getId(), channel);
			em.detach(channel);
		}
	}

	private void syncUpdatedData() {
		String q = "SELECT c FROM Channel c WHERE c.lastUpdateTime IS NOT NULL AND c.lastUpdateTime > :lastSyncDate ORDER BY c.lastUpdateTime DESC";
		List<Channel> updatedEntities = em.createQuery(q, Channel.class)
			.setParameter("lastSyncDate", lastSyncDate)
			.getResultList();
		updateCache(updatedEntities);
		if (!updatedEntities.isEmpty()) {
			lastSyncDate = updatedEntities.get(0).getLastUpdateTime();
		}
	}


	private void syncDeletedData() {
		Set<Long> ids = new HashSet<>(em.createQuery("SELECT c.id FROM Channel c", Long.class)
			.getResultList());
		Set<Long> toBeDeletedIds = new HashSet<>(cachedChannels.keySet());
		toBeDeletedIds.removeAll(ids);
		for (Long id : toBeDeletedIds) {
			cachedChannels.remove(id);
		}
	}
}

package com.azry.sps.server.services.channel;

import com.azry.sps.common.events.UpdateCacheEvent;
import com.azry.sps.common.exceptions.SPSException;
import com.azry.sps.common.model.channels.Channel;
import com.azry.sps.common.model.service.ServiceEntity;
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

//		return em.createQuery("select c FROM Channel c", Channel.class)
//			.getResultList();
	}

	@Override
	public List<Channel> getFilteredChannels(String name, Boolean isActive) {

		return cachingService.getFilteredChannels(name, isActive);

//		String sql = "FROM Channel c ";
//		Map<String, String> params = new HashMap<>();
//
//
//		if (isActive == null){
//			sql += "WHERE 1 = 1 ";
//		}
//
//		else if (isActive) {
//			sql += "WHERE c.active = TRUE ";
//		}
//
//		else {
//			sql += "WHERE c.active = FALSE ";
//		}
//
//		if (name != null && !name.trim().isEmpty()){
//			sql += "AND LOWER(c.name) like :name ";
//			params.put("name", "%" + name.toLowerCase() + "%");
//		}
//
//		TypedQuery<Channel> query = em.createQuery(sql, Channel.class);
//
//		for (Map.Entry<String, String> entry : params.entrySet()) {
//			query.setParameter(entry.getKey(), entry.getValue());
//		}
//
//		return query.getResultList();
	}

	@Override
	public Channel updateChannel(Channel channel) throws SPSException {
		try {
			return em.merge(channel);
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
		}
	}

	private void updateCache() {
		updateCacheEvent.fire(new UpdateCacheEvent(ServiceEntity.class.getSimpleName()));
	}
}

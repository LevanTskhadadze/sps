package com.azry.sps.server.services.channel;

import com.azry.sps.common.model.channels.Channel;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ChannelManagerBean implements ChannelManager {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Channel> getChannels() {
		return em.createQuery("select c FROM Channel c", Channel.class)
			.getResultList();
	}

	@Override
	public List<Channel> getFilteredChannels(String name, Boolean isActive) {
		String sql = "FROM Channel c ";
		Map<String, String> params = new HashMap<>();


		if (isActive == null){
			sql += "WHERE 1 = 1 ";
		}

		else if (isActive) {
			sql += "WHERE c.active = TRUE ";
		}

		else {
			sql += "WHERE c.active = FALSE ";
		}

		if (name != null && !name.trim().isEmpty()){
			sql += "AND LOWER(c.name) like :name ";
			params.put("name", "%" + name.toLowerCase() + "%");
		}

		TypedQuery<Channel> query = em.createQuery(sql, Channel.class);

		for (Map.Entry<String, String> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		return query.getResultList();
	}

	@Override
	public Channel updateChannel(Channel channel) {
		return em.merge(channel);
	}

	@Override
	public void deleteChannel(long id) {
		Channel channel = em.find(Channel.class, id);
		if (channel != null) {
			em.remove(channel);
		}
	}
}

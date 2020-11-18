package com.azry.sps.server.services.clientcommission;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.events.UpdateCacheEvent;
import com.azry.sps.common.exceptions.SPSException;
import com.azry.sps.common.model.commission.ClientCommissions;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ClientCommissionsManagerBean implements ClientCommissionsManager {

	@PersistenceContext
	EntityManager em;

	@Inject
	Event<UpdateCacheEvent> updateCacheEvent;

	@Override
	public ListResult<ClientCommissions> getFilteredClientCommissions(String serviceId, String channelId, int offset, int limit) {

		String sql = "FROM ClientCommissions c WHERE 1 = 1 ";
		Map<String, String> params = new HashMap<>();

		if (serviceId != null){
			if (serviceId.equals("-1")) {
				sql += "AND c.allServices = TRUE ";
			} else {
				sql += "AND (c.allServices = 1 OR FUNCTION('dbo.checkWord', :service, c.servicesIds, ',') = 1) ";
				params.put("service", serviceId);
			}
		}

		if (channelId != null){
			if (channelId.equals("-1")) {
				sql += "AND c.allChannels = TRUE ";
			} else {
				sql += "AND (c.allChannels = 1 OR FUNCTION('dbo.checkWord', :channel, c.channelsIds, ',') = 1) ";
				params.put("channel", channelId);
			}
		}

		TypedQuery<ClientCommissions> query = em.createQuery(sql, ClientCommissions.class);
		query.setFirstResult(offset);
		query.setMaxResults(limit);


		TypedQuery<Long> count = em.createQuery("SELECT COUNT(c.id) " + sql, Long.class);

		for (Map.Entry<String, String> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
			count.setParameter(entry.getKey(), entry.getValue());
		}

		List<ClientCommissions> clientCommissions = query.getResultList();

		return new ListResult<>(clientCommissions, (int)(long) count.getSingleResult());
	}

	public ClientCommissions getClientCommission(long serviceId){
		return em.createQuery("SELECT c FROM ClientCommissions c WHERE c.allServices = TRUE " +
			"OR FUNCTION('dbo.checkWord', :service, c.servicesIds, ',') = 1 ORDER BY c.priority", ClientCommissions.class)
			.setParameter("service",  serviceId)
			.setMaxResults(1)
			.getSingleResult();
	}

	@Override
	public ClientCommissions updateClientCommissions(ClientCommissions clientCommissions) throws SPSException {
		try {
			clientCommissions = em.merge(clientCommissions);
			UpdateCache();
			return clientCommissions;
		}
		catch (OptimisticLockException ex) {
			throw new SPSException("optimisticLockException", ex);
		}
	}

	@Override
	public void deleteClientCommissions(long id) {
		ClientCommissions clientCommissions = em.find(ClientCommissions.class, id);
		if (clientCommissions != null) {
			em.remove(clientCommissions);
			UpdateCache();
		}
	}

	private void UpdateCache() {
		updateCacheEvent.fire(new UpdateCacheEvent(ClientCommissions.class.getSimpleName()));
	}
}

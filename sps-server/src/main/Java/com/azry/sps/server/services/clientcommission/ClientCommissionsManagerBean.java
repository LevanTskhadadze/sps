package com.azry.sps.server.services.clientcommission;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.exceptions.SPSException;
import com.azry.sps.common.model.commission.ClientCommissions;

import javax.ejb.Stateless;
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

	@Override
	public ListResult<ClientCommissions> getFilteredClientCommissions(String serviceId, String channelId, int offset, int limit) {
		String sql = "FROM ClientCommissions c WHERE 1 = 1 ";
		Map<String, String> params = new HashMap<>();

		if (serviceId != null){
			if (serviceId.equals("-1")) {
				sql += "AND c.allServices = TRUE ";
			} else {
				sql += "AND c.allServices = TRUE OR c.servicesIds LIKE :firstService OR c.servicesIds LIKE :service ";
				params.put("firstService", serviceId + ",%");
				params.put("service", "%," + serviceId + ",%");
			}
		}

		if (channelId != null){
			if (channelId.equals("-1")) {
				sql += "AND c.allChannels = TRUE ";
			} else {
				sql += "AND c.allCannels = TRUE OR c.channelsIds LIKE :firstChannel OR c.channelsIds LIKE :channel ";
				params.put("firstChannel", channelId + ",%");
				params.put("channel", "%," + channelId + ",%");
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
		return em.createQuery("SELECT c FROM ClientCommissions c WHERE c.allServices = TRUE OR c.servicesIds " +
			"LIKE :firstService OR c.servicesIds LIKE :service ORDER BY c.priority", ClientCommissions.class)
			.setParameter("firstService", serviceId + ",%")
			.setParameter("service", "%," + serviceId + ",%")
			.setMaxResults(1)
			.getSingleResult();
	}

	@Override
	public ClientCommissions updateClientCommissions(ClientCommissions clientCommissions) throws SPSException {
		try {
			return em.merge(clientCommissions);
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
		}
	}
}

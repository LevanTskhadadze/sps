package com.azry.sps.server.services.clientcommission;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.model.commission.ClientCommissions;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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
				sql += "AND c.allServices = TRUE OR LOWER(c.servicesIds) LIKE :service ";
				params.put("service", "%" + serviceId + "%");
			}
		}

		if (channelId != null){
			if (channelId.equals("-1")) {
				sql += "AND c.allChannels = TRUE ";
			} else {
				sql += "AND c.allCannels = TRUE OR c.channelsIds LIKE :channel ";
				params.put("channel", "%" + channelId + "%");
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

	@Override
	public ClientCommissions updateClientCommissions(ClientCommissions clientCommissions) {
		return em.merge(clientCommissions);
	}

	@Override
	public void deleteClientCommissions(long id) {
		ClientCommissions clientCommissions = em.find(ClientCommissions.class, id);
		if (clientCommissions != null) {
			em.remove(clientCommissions);
		}
	}
}

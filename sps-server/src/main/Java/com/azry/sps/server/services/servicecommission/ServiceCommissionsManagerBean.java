package com.azry.sps.server.services.servicecommission;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.exceptions.SPSException;
import com.azry.sps.common.model.commission.ServiceCommissions;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ServiceCommissionsManagerBean implements ServiceCommissionsManager {

	@PersistenceContext
	EntityManager em;

	@Override
	public ListResult<ServiceCommissions> getFilteredServiceCommissions(String serviceId, int offset, int limit) {
		String sql = "FROM ServiceCommissions c WHERE 1 = 1 ";
		Map<String, String> params = new HashMap<>();

		if (serviceId != null){
			if (serviceId.equals("-1")) {
				sql += "AND c.allServices = TRUE ";
			} else {
				sql += "AND c.allServices = TRUE OR LOWER(c.servicesIds) LIKE :service ";
				params.put("service", "%" + serviceId + "%");
			}
		}

		TypedQuery<ServiceCommissions> query = em.createQuery(sql, ServiceCommissions.class);
		query.setFirstResult(offset);
		query.setMaxResults(limit);

		long count = em.createQuery("select COUNT(c.id) FROM ServiceCommissions c", Long.class).getSingleResult();

		for (Map.Entry<String, String> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		List<ServiceCommissions> serviceCommissions = query.getResultList();

		return new ListResult<>(serviceCommissions, (int) count);
	}

	@Override
	public ServiceCommissions updateServiceCommissions(ServiceCommissions serviceCommissions) throws SPSException {
		try {
			return em.merge(serviceCommissions);
		}
		catch (OptimisticLockException ex) {
			throw new SPSException("optimisticLockException", ex);
		}
	}

	@Override
	public void deleteServiceCommissions(long id) {
		ServiceCommissions serviceCommissions = em.find(ServiceCommissions.class, id);
		if (serviceCommissions != null) {
			em.remove(serviceCommissions);
		}
	}
}

package com.azry.sps.server.services.servicegroup;

import com.azry.sps.common.exception.SPSException;
import com.azry.sps.common.model.groups.ServiceGroup;
import com.azry.sps.common.model.service.Service;
import com.azry.sps.server.caching.CachedConfigurationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ServiceGroupManagerBean implements ServiceGroupManager {

	@PersistenceContext
	private EntityManager em;

	@Inject
	CachedConfigurationService cachingService;

	@Override
	public List<ServiceGroup> getFilteredServiceGroups(String name) {
		String sql = "FROM ServiceGroup g WHERE 1 = 1 ";
		Map<String, String> params = new HashMap<>();
		
		if (name != null && !name.trim().isEmpty()){
			sql += "AND LOWER(g.name) like :name ";
			params.put("name", "%" + name.toLowerCase() + "%");
		}

		TypedQuery<ServiceGroup> query = em.createQuery(sql + "ORDER BY g.priority", ServiceGroup.class);

		for (Map.Entry<String, String> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		return query.getResultList();
	}

	@Override
	public ServiceGroup updateServiceGroup(ServiceGroup serviceGroup) throws SPSException {
		try {
			return  em.merge(serviceGroup);
		}
		catch (OptimisticLockException ex) {
			throw new SPSException("optimisticLockException", ex);
		}
	}

	@Override
	public void deleteServiceGroup(Long id) throws SPSException {
		for (Service service: cachingService.getAllServices()) {
			if (id.equals(service.getGroupId())) {
				throw new SPSException("serviceGroupAlreadyUsedInServices");
			}
		}
		ServiceGroup serviceGroup = em.find(ServiceGroup.class, id);
		if (serviceGroup != null) {
			em.remove(serviceGroup);
		}
	}
}

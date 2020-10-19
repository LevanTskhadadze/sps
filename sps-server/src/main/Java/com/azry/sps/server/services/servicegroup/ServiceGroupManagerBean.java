package com.azry.sps.server.services.servicegroup;

import com.azry.sps.common.model.groups.ServiceGroup;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ServiceGroupManagerBean implements ServiceGroupManager {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<ServiceGroup> getFilteredServiceGroups(String name) {
		String sql = "FROM ServiceGroup g WHERE 1 = 1 ";
		Map<String, String> params = new HashMap<>();
		
		if (name != null && !name.trim().isEmpty()){
			sql += "AND LOWER(g.name) like :name ";
			params.put("name", "%" + name.toLowerCase() + "%");
		}

		Query query = em.createQuery(sql, ServiceGroup.class);

		for (Map.Entry<String, String> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		return query.getResultList();
	}

	@Override
	public ServiceGroup updateServiceGroup(ServiceGroup serviceGroup) {
		return  em.merge(serviceGroup);
	}

	@Override
	public void deleteServiceGroup(Long id) {
		ServiceGroup serviceGroup = em.find(ServiceGroup.class, id);
		if (serviceGroup != null) {
			em.remove(serviceGroup);
		}
	}
}

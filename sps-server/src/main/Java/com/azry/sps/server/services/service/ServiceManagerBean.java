package com.azry.sps.server.services.service;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.model.service.Service;
import com.azry.sps.common.model.service.ServiceEntity;
import com.azry.sps.common.model.users.SystemUser;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ServiceManagerBean implements ServiceManager {

	@PersistenceContext
	EntityManager em;

	@Override
	public ListResult<Service> getServices(Map<String, Object> params, int offset, int limit) {
		if (params == null) params = new HashMap<>();

		String queryPrefix = "SELECT se FROM ServiceEntity se ";
		String countPrefix = "SELECT COUNT(se.id) FROM ServiceEntity se ";
		StringBuilder str = new StringBuilder(" WHERE 1 = 1");
		Map<String, Object> values = new HashMap<>();


		if (params.containsKey("name") && params.get("name") != null && !params.get("name").equals("")) {
			values.put("name", params.get("name"));
			str.append(" AND se.name LIKE :name ");
		}

		if (params.containsKey("active") && params.get("active") != null && !params.get("active").equals("")) {
			values.put("active", params.get("active"));
			str.append(" AND se.active = :active ");
		}

		Query query = em.createQuery(queryPrefix + str.toString() + " ORDER BY se.lastUpdateTime DESC", ServiceEntity.class);
		Query count = em.createQuery(countPrefix + str.toString());
		query.setFirstResult(offset);
		query.setMaxResults(limit);

		for (Map.Entry<String, Object> entry : values.entrySet()) {

			if (entry.getKey().equals("active")) {
				query.setParameter(entry.getKey(), entry.getValue().equals("1"));
				count.setParameter(entry.getKey(), entry.getValue().equals("1"));
				continue;
			}
			query.setParameter(entry.getKey(), "%" + entry.getValue() + "%");
			count.setParameter(entry.getKey(), "%" + entry.getValue() + "%");
		}


		List<ServiceEntity> res = query.getResultList();
		List<Service> services = new ArrayList<>();
		for(ServiceEntity entity : res){
			services.add(entity.getService());
		}
		return new ListResult<>(services, (int)((long)count.getSingleResult()));

	}

	@Override
	public Service editService(Service service) {
		ServiceEntity entity = service.getEntity();
		return em.merge(entity).getService();
	}

	@Override
	public void removeService(long id) {
		ServiceEntity entity = em.find(ServiceEntity.class, id);
		em.remove(entity);
	}

	@Override
	public void changeActivation(long id) {
		ServiceEntity service = em.find(ServiceEntity.class, id);
		service.setActive(!service.isActive());

		em.persist(service);
	}
}
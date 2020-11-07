package com.azry.sps.server.services.service;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.exceptions.SPSException;
import com.azry.sps.common.model.service.Service;
import com.azry.sps.common.model.service.ServiceEntity;
import com.azry.sps.common.utils.XmlUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ServiceManagerBean implements ServiceManager {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<ServiceEntity> getAllServices() {

		return em.createQuery("SELECT s FROM ServiceEntity s", ServiceEntity.class)
			.getResultList();
	}

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

		TypedQuery<ServiceEntity> query = em.createQuery(queryPrefix + str.toString(), ServiceEntity.class);
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
	public Service editService(Service service) throws SPSException {
		try {
			ServiceEntity entity = service.getEntity();
			Service srv = em.merge(entity).getService();
			srv.setVersion(srv.getVersion() + 1);
			return srv;
		}
		catch (OptimisticLockException ex) {
			throw new SPSException("optimisticLockException");
		}
	}

	@Override
	public void removeService(long id) {
		ServiceEntity entity = em.find(ServiceEntity.class, id);
		em.remove(entity);
	}

	@Override
	public void changeActivation(long id, long version) throws SPSException {
		ServiceEntity service = em.find(ServiceEntity.class, id);
		if (version != service.getVersion()) {
			throw new SPSException("optimisticLockException");
		}
		service.setActive(!service.isActive());

		em.persist(service);
	}

	@Override
	public void setIcon(long id, String path) {
		ServiceEntity entity = em.find(ServiceEntity.class, id);
		Service srv = XmlUtils.fromXML(entity.getData(), Service.class);
		srv.setIcon(path);
		entity.setData(XmlUtils.toXml(srv));
		em.persist(entity);
	}

	@Override
	public String getIcon(long id) {
		ServiceEntity entity = em.find(ServiceEntity.class, id);
		Service srv = XmlUtils.fromXML(entity.getData(), Service.class);
		return srv.getIcon();
	}

	@Override
	public Service getService(long id) {
		return em.find(ServiceEntity.class, id).getService();
	}
}

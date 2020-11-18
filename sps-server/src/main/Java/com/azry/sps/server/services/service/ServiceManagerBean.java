package com.azry.sps.server.services.service;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.events.UpdateCacheEvent;
import com.azry.sps.common.exceptions.SPSException;
import com.azry.sps.common.model.service.Service;
import com.azry.sps.common.model.service.ServiceColumnNames;
import com.azry.sps.common.model.service.ServiceEntity;
import com.azry.sps.common.utils.XmlUtils;
import com.azry.sps.server.services.paymentlist.PaymentListManager;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
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

	@Inject
	Event<UpdateCacheEvent> updateCacheEventEvent;

	@PersistenceContext
	EntityManager em;

	@Inject
	PaymentListManager paymentListManager;


	@Override
	public List<ServiceEntity> getAllServiceEntities() {

		return em.createQuery("SELECT s FROM ServiceEntity s", ServiceEntity.class)
			.getResultList();
	}

	@Override
	public List<Service> getAllServices() {
		List<Service> services = new ArrayList<>();
		for (ServiceEntity serviceEntity : getAllServiceEntities()) {
			services.add(serviceEntity.getService());
		}
		return services;
	}

	@Override
	public ListResult<Service> getServices(Map<String, String> params, int offset, int limit) {
		if (params == null) params = new HashMap<>();

		String queryPrefix = "SELECT se FROM ServiceEntity se ";
		String countPrefix = "SELECT COUNT(se.id) FROM ServiceEntity se ";
		StringBuilder str = new StringBuilder(" WHERE 1 = 1");
		Map<String, Object> values = new HashMap<>();


		if (params.containsKey(ServiceColumnNames.NAME.getName())
					&& params.get(ServiceColumnNames.NAME.getName()) != null
					&& !params.get(ServiceColumnNames.NAME.getName()).equals("")) {
			values.put(ServiceColumnNames.NAME.getName(), params.get(ServiceColumnNames.NAME.getName()));
			str.append(" AND se.name LIKE :name ");
		}

		if (params.containsKey(ServiceColumnNames.ACTIVE.getName())
					&& params.get(ServiceColumnNames.ACTIVE.getName()) != null
					&& !params.get(ServiceColumnNames.ACTIVE.getName()).equals("")) {
			values.put(ServiceColumnNames.ACTIVE.getName(), params.get(ServiceColumnNames.ACTIVE.getName()));
			str.append(" AND se.active = :active ");
		}

		TypedQuery<ServiceEntity> query = em.createQuery(queryPrefix + str.toString(), ServiceEntity.class);
		Query count = em.createQuery(countPrefix + str.toString());
		query.setFirstResult(offset);
		query.setMaxResults(limit);

		for (Map.Entry<String, Object> entry : values.entrySet()) {

			if (entry.getKey().equals(ServiceColumnNames.ACTIVE.getName())) {
				query.setParameter(entry.getKey(), entry.getValue().equals(ServiceColumnNames.ActivationStatus.ACTIVE.getStatus()));
				count.setParameter(entry.getKey(), entry.getValue().equals(ServiceColumnNames.ActivationStatus.ACTIVE.getStatus()));
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
	public List<Service> getServicesByServiceGroup(long groupId) {
		List <Service> services = new ArrayList<>();
		List<ServiceEntity> serviceEntities = getAllServiceEntities();
		for (ServiceEntity serviceEntity : serviceEntities) {
			if (serviceEntity.getService().getGroupId() == groupId) {
				services.add(serviceEntity.getService());
			}
		}
		return services;
	}

	@Override
	public Service editService(Service service) throws SPSException {
		try {
			ServiceEntity entity = service.getEntity();
			Service srv = em.merge(entity).getService();
			srv.setVersion(srv.getVersion() + 1);
			updateCache();
			return srv;
		}
		catch (OptimisticLockException ex) {
			throw new SPSException("optimisticLockException");
		}
	}

	@Override
	public void removeService(long id) {
		ServiceEntity entity = em.find(ServiceEntity.class, id);
		if (entity != null) {
			em.remove(entity);
			paymentListManager.deletePaymentListEntriesByServiceId(id);
			updateCache();
		}
	}

	@Override
	public void changeActivation(long id, long version) throws SPSException {
		ServiceEntity service = em.find(ServiceEntity.class, id);
		if (version != service.getVersion()) {
			throw new SPSException("optimisticLockException");
		}
		service.setActive(!service.isActive());

		em.persist(service);
		updateCache();
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

	private void updateCache() {
		updateCacheEventEvent.fire(new UpdateCacheEvent(ServiceEntity.class.getSimpleName()));
	}
}

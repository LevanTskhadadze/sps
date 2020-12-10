package com.azry.sps.server.services.service;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.events.UpdateCacheEvent;
import com.azry.sps.common.exception.SPSException;
import com.azry.sps.common.model.commission.ClientCommissions;
import com.azry.sps.common.model.commission.ServiceCommissions;
import com.azry.sps.common.model.service.Service;
import com.azry.sps.common.model.service.ServiceEntity;
import com.azry.sps.common.utils.XmlUtils;
import com.azry.sps.server.caching.CachedConfigurationService;
import com.azry.sps.server.services.paymentlist.PaymentListManager;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
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

	@Inject
	CachedConfigurationService cachingService;

//	@Override
//	public List<ServiceEntity> getAllServiceEntities() {
//		return em.createQuery("SELECT s FROM ServiceEntity s", ServiceEntity.class)
//			.getResultList();
//	}

	@Override
	public List<Service> getAllServices() {
		return cachingService.getAllServices();
	}

	@Override
	public List<Service> getAllActiveServices() {
		return cachingService.getAllActiveServices();
	}

	@Override
	public ListResult<Service> getServices(Map<String, String> params, int offset, int limit) {
		if (params == null) params = new HashMap<>();
		return cachingService.filterServices(params, offset, limit);
	}

	@Override
	public List<Service> getServicesByServiceGroup(Long groupId) {
		return cachingService.getServicesByServiceGroup(groupId);
	}

	@Override
	public Service editService(Service service) throws SPSException {
		try {
			int add = 1;
			ServiceEntity entity = service.getEntity();
			if (em.find(ServiceEntity.class, entity.getId()) == null) {
				add = 0;
			}
			Service srv = em.merge(entity).getService();
			srv.setVersion(srv.getVersion() + add);
			updateCache();
			return srv;
		}
		catch (OptimisticLockException ex) {
			throw new SPSException("optimisticLockException");
		}
	}

	@Override
	public void removeService(long id) throws SPSException {
		for (ClientCommissions clientCommission : cachingService.getAllClientCommissions()) {
			List<String> serviceIds = new ArrayList<>(Arrays.asList(clientCommission.getServicesIds().split(",")));
			serviceIds.removeIf(serviceId -> serviceId.equals(String.valueOf(id)));
			clientCommission.setServicesIds(String.join(",", serviceIds));
			em.merge(clientCommission);
		}
		updateCacheEventEvent.fire(new UpdateCacheEvent(ClientCommissions.class.getSimpleName()));

		for (ServiceCommissions serviceCommission: cachingService.getAllServiceCommissions()) {
			List<String> serviceIds = new ArrayList<>(Arrays.asList(serviceCommission.getServicesIds().split(",")));
			serviceIds.removeIf(serviceId -> serviceId.equals(String.valueOf(id)));
			serviceCommission.setServicesIds(String.join(",", serviceIds));
			em.merge(serviceCommission);
		}
		updateCacheEventEvent.fire(new UpdateCacheEvent(ServiceCommissions.class.getSimpleName()));

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
		//ServiceEntity entity = em.find(ServiceEntity.class, id);
		Service srv = cachingService.getService(id);

//		if (entity == null) {
//			srv = new Service();
//		}
//		srv = XmlUtils.fromXML(entity.getData(), Service.class);
		return srv.getIcon();
	}

	@Override
	public Service getService(long id) {
		return cachingService.getService(id);
	}

	@Override
	public Service getServiceByPaymentCode(String servicePaymentCode) {
		return cachingService.getServiceByPayCode(servicePaymentCode);
	}

	private void updateCache() {
		updateCacheEventEvent.fire(new UpdateCacheEvent(ServiceEntity.class.getSimpleName()));
	}
}

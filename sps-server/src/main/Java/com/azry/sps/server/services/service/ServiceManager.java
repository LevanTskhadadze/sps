package com.azry.sps.server.services.service;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.exception.SPSException;
import com.azry.sps.common.model.service.Service;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;

@Local
public interface ServiceManager {

	//List<ServiceEntity> getAllServiceEntities();

	List<Service> getAllServices();

	List<Service> getAllActiveServices();

	ListResult<Service> getServices(Map<String, String> params, int offset, int limit);

	List<Service> getServicesByServiceGroup(Long groupId);

	Service editService(Service service) throws SPSException;

	void removeService(long id) throws SPSException;

	void changeActivation(long id, long version) throws SPSException;

	void setIcon(long id, String path);

	String getIcon(long id);

    Service getService(long id);
}

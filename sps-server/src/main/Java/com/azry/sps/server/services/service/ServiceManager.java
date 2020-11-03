package com.azry.sps.server.services.service;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.exceptions.SPSException;
import com.azry.sps.common.model.service.Service;
import com.azry.sps.common.model.service.ServiceEntity;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;

@Local
public interface ServiceManager {

	List<ServiceEntity> getAllServices();

	ListResult<Service> getServices(Map<String, Object> params, int offset, int limit);

	Service editService(Service service) throws SPSException;

	void removeService(long id);

	void changeActivation(long id) throws SPSException;

	void setIcon(long id, String path);

	String getIcon(long id);

    Service getService(long id);
}

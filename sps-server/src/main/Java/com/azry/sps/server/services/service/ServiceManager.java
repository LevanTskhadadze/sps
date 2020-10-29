package com.azry.sps.server.services.service;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.model.service.Service;

import javax.ejb.Local;
import java.util.Map;

@Local
public interface ServiceManager {

	ListResult<Service> getServices(Map<String, Object> params, int offset, int limit);

	Service editService(Service service);

	void removeService(long id);

	void changeActivation(long id);

	void setIcon(long id, String path);

	String getIcon(long id);

    Service getService(long id);
}

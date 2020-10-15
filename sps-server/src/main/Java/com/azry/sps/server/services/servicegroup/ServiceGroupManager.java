package com.azry.sps.server.services.servicegroup;

import com.azry.sps.common.model.service.ServiceGroup;

import javax.ejb.Local;
import java.util.List;

@Local
public interface ServiceGroupManager {

	List<ServiceGroup> getServiceGroups();

	List<ServiceGroup> getFilteredServiceGroups(String name);

	ServiceGroup updateServiceGroup(ServiceGroup serviceGroup);

	void deleteServiceGroup(Long id);
}

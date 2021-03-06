package com.azry.sps.server.services.servicegroup;

import com.azry.sps.common.exception.SPSException;
import com.azry.sps.common.model.groups.ServiceGroup;

import javax.ejb.Local;
import java.util.List;

@Local
public interface ServiceGroupManager {

	List<ServiceGroup> getFilteredServiceGroups(String name);

	ServiceGroup updateServiceGroup(ServiceGroup serviceGroup) throws SPSException;

	void deleteServiceGroup(Long id) throws SPSException;
}

package com.azry.sps.server.services.servicecommission;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.exceptions.SPSException;
import com.azry.sps.common.model.commission.ServiceCommissions;

import javax.ejb.Local;

@Local
public interface ServiceCommissionsManager {

	ListResult<ServiceCommissions> getFilteredServiceCommissions(String service, int offset, int limit);

	ServiceCommissions updateServiceCommissions(ServiceCommissions serviceCommissions) throws SPSException;

	void deleteServiceCommissions(long id);
}

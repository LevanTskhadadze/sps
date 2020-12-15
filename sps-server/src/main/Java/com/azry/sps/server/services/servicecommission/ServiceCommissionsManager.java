package com.azry.sps.server.services.servicecommission;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.exception.SPSException;
import com.azry.sps.common.model.commission.ServiceCommissions;

import javax.ejb.Local;
import java.math.BigDecimal;

@Local
public interface ServiceCommissionsManager {

	ListResult<ServiceCommissions> getFilteredServiceCommissions(String service, int offset, int limit);

	ServiceCommissions updateServiceCommissions(ServiceCommissions serviceCommissions) throws SPSException;

	ServiceCommissions getCommissionForService(long serviceId);

	BigDecimal calculateCommission(long serviceId, BigDecimal principal);

	void deleteServiceCommissions(long id);
}

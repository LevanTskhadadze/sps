package com.azry.sps.console.server;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.exception.SPSException;
import com.azry.sps.common.model.commission.ServiceCommissions;
import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.commission.servicecommission.ServiceCommissionsDTO;
import com.azry.sps.console.shared.servicecommission.ServiceCommissionsService;
import com.azry.sps.server.services.servicecommission.ServiceCommissionsManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;

@WebServlet("sps/servlet/ServiceCommissions")
public class ServiceCommissionsServiceImpl extends RemoteServiceServlet implements ServiceCommissionsService {

	@Inject
	ServiceCommissionsManager serviceCommissionsManager;

	@Override
	public PagingLoadResult<ServiceCommissionsDTO> getServiceCommissions(String serviceId, PagingLoadConfig config) {
		ListResult<ServiceCommissions> listResult =  serviceCommissionsManager.getFilteredServiceCommissions(serviceId, config.getOffset(), config.getLimit());
		return new  PagingLoadResultBean<>(ServiceCommissionsDTO.toDTOs(listResult.getResultList()), listResult.getResultCount(), config.getOffset());
	}

	@Override
	public ServiceCommissionsDTO updateServiceCommissions(ServiceCommissionsDTO serviceCommissions) throws SPSConsoleException {
		try {
			return ServiceCommissionsDTO.toDTO(serviceCommissionsManager.updateServiceCommissions(serviceCommissions.fromDTO()));
		}
		catch (SPSException ex) {
			throw new SPSConsoleException(ex);
		}
	}

	@Override
	public void deleteServiceCommissions(long id) {
		serviceCommissionsManager.deleteServiceCommissions(id);
	}
}

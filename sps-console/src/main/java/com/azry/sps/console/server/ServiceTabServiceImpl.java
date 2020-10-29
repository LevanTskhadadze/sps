package com.azry.sps.console.server;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.model.service.Service;
import com.azry.sps.common.model.service.ServiceEntity;
import com.azry.sps.common.model.users.SystemUser;
import com.azry.sps.console.shared.dto.services.ServiceDto;
import com.azry.sps.console.shared.dto.users.SystemUserDTO;
import com.azry.sps.console.shared.service.ServiceTabService;
import com.azry.sps.server.services.service.ServiceManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import java.util.List;
import java.util.Map;

@WebServlet("sps/servlet/serviceTab")
public class ServiceTabServiceImpl extends RemoteServiceServlet implements ServiceTabService {

	@Inject
	ServiceManager serviceManager;

	@Override
	public PagingLoadResult<ServiceDto> getServices(Map<String, Object> params, int offset, int limit) {
		ListResult<Service> result = serviceManager.getServices(params, offset, limit);
		List<ServiceDto> res = ServiceDto.toDtos(result.getResultList());
		return new PagingLoadResultBean<>(
			res,
			result.getResultCount(),
			offset
		);
	}

	@Override
	public ServiceDto getService(long id){
		return ServiceDto.toDto(serviceManager.getService(id));
	}

	@Override
	public ServiceDto editService(ServiceDto service) {
		return ServiceDto.toDto(serviceManager.editService(ServiceDto.toEntity(service)));
	}

	@Override
	public void removeService(long id) {
		serviceManager.removeService(id);
	}

	@Override
	public void changeActivation(long id) {
		serviceManager.changeActivation(id);
	}
}

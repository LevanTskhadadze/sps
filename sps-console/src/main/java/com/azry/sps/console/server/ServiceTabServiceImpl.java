package com.azry.sps.console.server;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.exceptions.SPSException;
import com.azry.sps.common.model.service.Service;
import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.services.ServiceDto;
import com.azry.sps.console.shared.dto.services.ServiceEntityDto;
import com.azry.sps.console.shared.service.ServiceTabService;
import com.azry.sps.server.services.service.ServiceManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@WebServlet("sps/servlet/serviceTab")
public class ServiceTabServiceImpl extends RemoteServiceServlet implements ServiceTabService {

	@Inject
	ServiceManager serviceManager;

	public List<ServiceEntityDto> getAllServices(){
		return ServiceEntityDto.toDtos(serviceManager.getAllServices());
	}

	Comparator<Service> serviceComparator = new Comparator<Service>() {
		@Override
		public int compare(Service s1, Service s2) {
			return s1.getName().compareTo(s2.getName());
		}
	};

	@Override
	public PagingLoadResult<ServiceDto> getServices(Map<String, Object> params, int offset, int limit) {
		ListResult<Service> result = serviceManager.getServices(params, offset, limit);
		Collections.sort(result.getResultList(), serviceComparator);
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
	public ServiceDto editService(ServiceDto service) throws SPSConsoleException {
		try {
			return ServiceDto.toDto(serviceManager.editService(ServiceDto.toEntity(service)));
		}
		catch (SPSException ex) {
			throw new SPSConsoleException(ex);
		}
	}

	@Override
	public void removeService(long id) {
		serviceManager.removeService(id);
	}

	@Override
	public void changeActivation(long id, long version) throws SPSConsoleException {
		try {
			serviceManager.changeActivation(id, version);
		}
		catch (SPSException ex) {
			throw new SPSConsoleException(ex);
		}
	}
}

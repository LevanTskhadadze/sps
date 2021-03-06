package com.azry.sps.console.server;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.exception.SPSException;
import com.azry.sps.common.model.service.Service;
import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.services.ServiceDTO;
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


	Comparator<Service> serviceComparator = new Comparator<Service>() {
		@Override
		public int compare(Service s1, Service s2) {
			return s1.getName().compareTo(s2.getName());
		}
	};

	@Override
	public List<ServiceDTO> getAllServices() {
		return ServiceDTO.toDTOs(serviceManager.getAllServices());
	}

	@Override
	public List<ServiceDTO> getAllActiveServices() {
		return ServiceDTO.toDTOs(serviceManager.getAllActiveServices());
	}

	@Override
	public PagingLoadResult<ServiceDTO> getServices(Map<String, String> params, int offset, int limit) {
		ListResult<Service> result = serviceManager.getServices(params, offset, limit);
		Collections.sort(result.getResultList(), serviceComparator);
		List<ServiceDTO> res = ServiceDTO.toDTOs(result.getResultList());
		return new PagingLoadResultBean<>(
			res,
			result.getResultCount(),
			offset
		);
	}

	@Override
	public List<ServiceDTO> getServicesByServiceGroup(Long groupId) {
		return ServiceDTO.toDTOs(serviceManager.getServicesByServiceGroup(groupId));
	}

	@Override
	public ServiceDTO getService(long id){
		return ServiceDTO.toDTO(serviceManager.getService(id));
	}

	@Override
	public ServiceDTO editService(ServiceDTO service) throws SPSConsoleException {
		try {
			return ServiceDTO.toDTO(serviceManager.editService(ServiceDTO.toEntity(service)));
		}
		catch (SPSException ex) {
			throw new SPSConsoleException(ex);
		}
	}

	@Override
	public void removeService(long id) throws SPSConsoleException {
		try {
			serviceManager.removeService(id);
		} catch (SPSException ex) {
			throw new SPSConsoleException(ex);
		}
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

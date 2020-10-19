package com.azry.sps.console.server;

import com.azry.sps.common.model.groups.ServiceGroup;
import com.azry.sps.console.shared.dto.servicegroup.ServiceGroupDTO;
import com.azry.sps.console.shared.servicegroup.ServiceGroupService;
import com.azry.sps.server.services.servicegroup.ServiceGroupManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import java.util.List;

@WebServlet("sps/servlet/ServiceGroup")
public class ServiceGroupServiceImpl extends RemoteServiceServlet implements ServiceGroupService {

	@Inject
	private ServiceGroupManager serviceGroupManager;

	@Override
	public List<ServiceGroupDTO> getFilteredServiceGroups(String name) {
		return ServiceGroupDTO.toDTOs(serviceGroupManager.getFilteredServiceGroups(name));
	}

	@Override
	public ServiceGroupDTO updateServiceGroup(ServiceGroupDTO dto) {
			ServiceGroup serviceGroup = serviceGroupManager.updateServiceGroup(dto.fromDTO());
			return ServiceGroupDTO.toDTO(serviceGroup);
	}

	@Override
	public void deleteServiceGroup(long id) {
		serviceGroupManager.deleteServiceGroup(id);
	}
}

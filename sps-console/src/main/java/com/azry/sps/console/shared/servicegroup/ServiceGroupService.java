package com.azry.sps.console.shared.servicegroup;

import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.servicegroup.ServiceGroupDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("servlet/ServiceGroup")
public interface ServiceGroupService extends RemoteService {

	List<ServiceGroupDTO> getFilteredServiceGroups(String name);

	ServiceGroupDTO updateServiceGroup(ServiceGroupDTO dto) throws SPSConsoleException;

	void deleteServiceGroup(long id) throws SPSConsoleException;
}

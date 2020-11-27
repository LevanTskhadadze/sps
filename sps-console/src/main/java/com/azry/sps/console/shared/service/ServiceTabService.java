package com.azry.sps.console.shared.service;

import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.services.ServiceDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import java.util.List;
import java.util.Map;

@RemoteServiceRelativePath("servlet/serviceTab")
public interface ServiceTabService extends RemoteService {

	List<ServiceDTO> getAllServices();

	List<ServiceDTO> getAllActiveServices();

	PagingLoadResult<ServiceDTO> getServices(Map<String, String> params, int offset, int limit);

	List<ServiceDTO> getServicesByServiceGroup(Long groupId);

	ServiceDTO editService(ServiceDTO service) throws SPSConsoleException;

	void removeService(long id);

	ServiceDTO getService(long id);

	void changeActivation(long id, long version) throws SPSConsoleException;
}

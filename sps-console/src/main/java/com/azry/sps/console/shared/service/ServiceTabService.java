package com.azry.sps.console.shared.service;

import com.azry.sps.console.shared.dto.services.ServiceDto;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import java.util.Map;

@RemoteServiceRelativePath("servlet/serviceTab")
public interface ServiceTabService extends RemoteService {

	PagingLoadResult<ServiceDto> getServices(Map<String, Object> params, int offset, int limit);

	ServiceDto editService(ServiceDto service);

	void removeService(long id);

	void changeActivation(long id);
}

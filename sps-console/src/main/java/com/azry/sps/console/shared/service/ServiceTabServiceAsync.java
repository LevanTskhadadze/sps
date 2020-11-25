package com.azry.sps.console.shared.service;

import com.azry.sps.console.shared.dto.services.ServiceDto;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import java.util.List;
import java.util.Map;

public interface ServiceTabServiceAsync {

	void getAllServices(AsyncCallback<List<ServiceDto>> callback);

	void getAllActiveServices(AsyncCallback<List<ServiceDto>> callback);

	void getServices(Map<String, String> params, int offset, int limit, AsyncCallback<PagingLoadResult<ServiceDto>> callback);

	void getServicesByServiceGroup(Long groupId, AsyncCallback<List<ServiceDto>> callback);

	void getService(long id, AsyncCallback<ServiceDto> callback);

	void editService(ServiceDto service, AsyncCallback<ServiceDto> callback);

	void removeService(long id, AsyncCallback<Void> callback);

	void changeActivation(long id, long version, AsyncCallback<Void> callback);
}

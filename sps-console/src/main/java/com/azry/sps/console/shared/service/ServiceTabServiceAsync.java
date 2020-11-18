package com.azry.sps.console.shared.service;

import com.azry.sps.console.shared.dto.services.ServiceDto;
import com.azry.sps.console.shared.dto.services.ServiceEntityDto;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import java.util.List;
import java.util.Map;

public interface ServiceTabServiceAsync {

	void getAllServiceEntities(AsyncCallback<List<ServiceEntityDto>> callback);

	void getAllServices(AsyncCallback<List<ServiceDto>> async);

	void getServices(Map<String, String> params, int offset, int limit, AsyncCallback<PagingLoadResult<ServiceDto>> callback);

	void getServicesByServiceGroup(long groupId, AsyncCallback<List<ServiceDto>> async);

	void getService(long id, AsyncCallback<ServiceDto> callback);

	void editService(ServiceDto service, AsyncCallback<ServiceDto> callback);

	void removeService(long id, AsyncCallback<Void> callback);

	void changeActivation(long id, long version, AsyncCallback<Void> callback);
}

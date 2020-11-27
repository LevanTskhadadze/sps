package com.azry.sps.console.shared.service;

import com.azry.sps.console.shared.dto.services.ServiceDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import java.util.List;
import java.util.Map;

public interface ServiceTabServiceAsync {

	void getAllServices(AsyncCallback<List<ServiceDTO>> callback);

	void getAllActiveServices(AsyncCallback<List<ServiceDTO>> callback);

	void getServices(Map<String, String> params, int offset, int limit, AsyncCallback<PagingLoadResult<ServiceDTO>> callback);

	void getServicesByServiceGroup(Long groupId, AsyncCallback<List<ServiceDTO>> callback);

	void getService(long id, AsyncCallback<ServiceDTO> callback);

	void editService(ServiceDTO service, AsyncCallback<ServiceDTO> callback);

	void removeService(long id, AsyncCallback<Void> callback);

	void changeActivation(long id, long version, AsyncCallback<Void> callback);
}

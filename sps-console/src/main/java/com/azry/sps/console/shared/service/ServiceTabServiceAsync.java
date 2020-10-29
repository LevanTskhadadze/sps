package com.azry.sps.console.shared.service;

import com.azry.sps.console.shared.dto.services.ServiceDto;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import java.util.Map;

public interface ServiceTabServiceAsync {

	void getServices(Map<String, Object> params, int offset, int limit, AsyncCallback<PagingLoadResult<ServiceDto>> callback);

	void getService(long id, AsyncCallback<ServiceDto> callback);

	void editService(ServiceDto service, AsyncCallback<ServiceDto> callback);

	void removeService(long id, AsyncCallback<Void> callback);

	void changeActivation(long id, AsyncCallback<Void> callback);
}

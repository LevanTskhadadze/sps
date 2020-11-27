package com.azry.sps.console.shared.servicecommission;

import com.azry.sps.console.shared.dto.commission.servicecommission.ServiceCommissionsDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

public interface ServiceCommissionsServiceAsync {

	void getServiceCommissions(String serviceId, PagingLoadConfig config, AsyncCallback<PagingLoadResult<ServiceCommissionsDTO>> async);

	void updateServiceCommissions(ServiceCommissionsDTO clientCommissions, AsyncCallback<ServiceCommissionsDTO> async);

	void deleteServiceCommissions(long id, AsyncCallback<Void> async);
}

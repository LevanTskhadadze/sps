package com.azry.sps.console.shared.servicecommission;

import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.commission.servicecommission.ServiceCommissionsDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

@RemoteServiceRelativePath("servlet/ServiceCommissions")
public interface ServiceCommissionsService extends RemoteService {

	PagingLoadResult<ServiceCommissionsDTO> getServiceCommissions(String serviceID, PagingLoadConfig config);

	ServiceCommissionsDTO updateServiceCommissions(ServiceCommissionsDTO clientCommissions) throws SPSConsoleException;

	void deleteServiceCommissions(long id);
}

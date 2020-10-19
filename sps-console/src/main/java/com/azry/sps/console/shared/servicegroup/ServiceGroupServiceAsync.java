package com.azry.sps.console.shared.servicegroup;

import com.azry.sps.console.shared.dto.servicegroup.ServiceGroupDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface ServiceGroupServiceAsync {

    void getFilteredServiceGroups(String name, AsyncCallback<List<ServiceGroupDTO>> async);

    void updateServiceGroup(ServiceGroupDTO dto, AsyncCallback<ServiceGroupDTO> async);

    void deleteServiceGroup(long id, AsyncCallback<Void> async);
}

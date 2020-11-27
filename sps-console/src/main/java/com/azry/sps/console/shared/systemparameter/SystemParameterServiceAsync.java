package com.azry.sps.console.shared.systemparameter;

import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;
import java.util.Map;

public interface SystemParameterServiceAsync {
	void getSystemParameterTab(Map<String, String> params, AsyncCallback<List<SystemParameterDTO>> callback);

	void removeParameter(long id, AsyncCallback<Void> callback);

    void editParameter(SystemParameterDTO dto, AsyncCallback<Void> callback);

    void getParameter(long id, AsyncCallback<SystemParameterDTO> callback);

    void addParameter(SystemParameterDTO dto, AsyncCallback<SystemParameterDTO> callback);
}

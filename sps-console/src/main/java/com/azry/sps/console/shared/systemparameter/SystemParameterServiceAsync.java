package com.azry.sps.console.shared.systemparameter;

import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDto;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDtoType;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;
import java.util.Map;

public interface SystemParameterServiceAsync {
	void getSystemParameterTab(Map<String, String> params, AsyncCallback<List<SystemParameterDto>> callback);

	void removeParameter(long id, AsyncCallback<Void> callback);

    void editParameter(long id, String code, String type, String value, String description, AsyncCallback<Void> callback);

    void getParameter(long id, AsyncCallback<SystemParameterDto> callback);

    void addParameter(String code, SystemParameterDtoType type, String value, String desc, AsyncCallback<SystemParameterDto> callback);
}

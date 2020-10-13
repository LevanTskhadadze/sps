package com.azry.sps.console.shared.systemparameters;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface TabServiceAsync {
	void getSystemParameterTab(AsyncCallback<List<SystemParameterDto>> callback);

}

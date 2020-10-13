package com.azry.sps.console.shared.systemparameters;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("servlet/getTab")
public interface TabService extends RemoteService {
	List<SystemParameterDto> getSystemParameterTab();

}

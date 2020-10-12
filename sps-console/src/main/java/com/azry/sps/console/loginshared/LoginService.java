package com.azry.sps.console.loginshared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("servlet/login")
public interface LoginService extends RemoteService {

	boolean login(String username, String password);
}
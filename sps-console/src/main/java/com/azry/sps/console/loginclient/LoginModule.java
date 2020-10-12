package com.azry.sps.console.loginclient;

import com.azry.sps.console.loginshared.LoginService;
import com.azry.sps.console.loginshared.LoginServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class LoginModule implements EntryPoint {

	public LoginServiceAsync loginService = GWT.create(LoginService.class);

	@Override
	public void onModuleLoad() {

		LoginView view  = new LoginView((loginService));

		RootPanel.get().add(view);

	}
}
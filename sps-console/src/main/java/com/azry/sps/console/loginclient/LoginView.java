package com.azry.sps.console.loginclient;

import com.azry.sps.console.loginshared.LoginServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;

public class LoginView extends Composite {

	private static LoginPageUiBinder uiBinder = GWT.create(LoginPageUiBinder.class);

	interface LoginPageUiBinder extends UiBinder<Widget, LoginView> {}

	@UiField
	HTMLPanel mainPanel;

	@UiField
	HeadingElement headingElement;

	@UiField
	InputElement j_username;

	@UiField
	InputElement j_password;

	@UiField
	ButtonElement loginButton;

	private LoginServiceAsync loginService;

	public LoginView(LoginServiceAsync loginService) {
		this.loginService = loginService;
		init();
	}

	private void init() {
		initWidget(uiBinder.createAndBindUi(this));

		mainPanel.getElement().getStyle().setProperty("overflow", "auto");
		DOM.sinkEvents(loginButton, Event.ONCLICK);
		DOM.setEventListener(loginButton, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (event.getTypeInt() == Event.ONCLICK) {
					event.preventDefault();
					String username = j_username.getValue();
					String password = j_password.getValue();

					loginService.login(username, password, new AsyncCallback<Boolean>() {
						@Override
						public void onSuccess(Boolean result) {
							if (result) {
								Window.Location.replace("index.jsp");
							} else {
								headingElement.setInnerText("მომხმარებლის სახელი ან პაროლი არასწორია!");
							}
						}

						@Override
						public void onFailure(Throwable th) {
							headingElement.setInnerText("მომხმარებლის სახელი ან პაროლი არასწორია!");
						}
					});
				}
			}
		});
	}
}
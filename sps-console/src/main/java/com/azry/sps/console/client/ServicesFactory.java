package com.azry.sps.console.client;

import com.azry.sps.console.shared.servicegroup.ServiceGroupService;
import com.azry.sps.console.shared.servicegroup.ServiceGroupServiceAsync;
import com.azry.sps.console.shared.users.UserService;
import com.azry.sps.console.shared.users.UserServiceAsync;
import com.google.gwt.core.client.GWT;

public class ServicesFactory {

    private static UserServiceAsync userService;

    public static UserServiceAsync getUserService() {
        if (userService == null) {
            userService = GWT.create(UserService.class);
        }
        return userService;
    }

	private static ServiceGroupServiceAsync serviceGroupService;

	public static ServiceGroupServiceAsync getServiceGroupService() {
		if (serviceGroupService == null) {
			serviceGroupService = GWT.create(ServiceGroupService.class);
		}
		return serviceGroupService;
	}
}
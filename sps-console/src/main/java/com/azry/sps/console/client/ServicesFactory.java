package com.azry.sps.console.client;

import com.azry.sps.common.model.users.UserGroup;
import com.azry.sps.console.shared.servicegroup.ServiceGroupService;
import com.azry.sps.console.shared.servicegroup.ServiceGroupServiceAsync;
import com.azry.sps.console.shared.usergroup.UserGroupService;
import com.azry.sps.console.shared.usergroup.UserGroupServiceAsync;
import com.azry.sps.console.shared.users.UserService;
import com.azry.sps.console.shared.users.UserServiceAsync;
import com.azry.sps.console.shared.usertab.UserTabService;
import com.azry.sps.console.shared.usertab.UserTabServiceAsync;
import com.google.gwt.core.client.GWT;

import com.azry.sps.console.shared.systemparameter.SystemParameterService;
import com.azry.sps.console.shared.systemparameter.SystemParameterServiceAsync;

public class ServicesFactory {
	private static SystemParameterServiceAsync systemParameterService;
	public static SystemParameterServiceAsync getSystemParameterService() {
		if (systemParameterService == null) {
			systemParameterService =  GWT.create(SystemParameterService.class);
		}
		return systemParameterService;
	}

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


	private static UserTabServiceAsync userTabService;

	public static UserTabServiceAsync getUserTabService() {
		if (userTabService == null) {
			userTabService = GWT.create(UserTabService.class);
		}
		return userTabService;
	}


	private static UserGroupServiceAsync userGroupService;

	public static UserGroupServiceAsync getUserGroupService() {
		if (userGroupService == null) {
			userGroupService = GWT.create(UserGroupService.class);
		}
		return userGroupService;
	}
}
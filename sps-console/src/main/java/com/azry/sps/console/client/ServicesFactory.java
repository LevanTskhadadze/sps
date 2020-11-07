package com.azry.sps.console.client;


import com.azry.sps.console.shared.bankintegration.BankIntegrationService;
import com.azry.sps.console.shared.bankintegration.BankIntegrationServiceAsync;
import com.azry.sps.console.shared.channel.ChannelService;
import com.azry.sps.console.shared.channel.ChannelServiceAsync;
import com.azry.sps.console.shared.clientcommission.ClientCommissionsService;
import com.azry.sps.console.shared.clientcommission.ClientCommissionsServiceAsync;
import com.azry.sps.console.shared.service.ServiceTabService;
import com.azry.sps.console.shared.service.ServiceTabServiceAsync;
import com.azry.sps.console.shared.servicecommission.ServiceCommissionsService;
import com.azry.sps.console.shared.servicecommission.ServiceCommissionsServiceAsync;
import com.azry.sps.console.shared.servicegroup.ServiceGroupService;
import com.azry.sps.console.shared.servicegroup.ServiceGroupServiceAsync;
import com.azry.sps.console.shared.systemparameter.SystemParameterService;
import com.azry.sps.console.shared.systemparameter.SystemParameterServiceAsync;
import com.azry.sps.console.shared.usergroup.UserGroupService;
import com.azry.sps.console.shared.usergroup.UserGroupServiceAsync;
import com.azry.sps.console.shared.users.UserService;
import com.azry.sps.console.shared.users.UserServiceAsync;
import com.azry.sps.console.shared.usertab.UserTabService;
import com.azry.sps.console.shared.usertab.UserTabServiceAsync;
import com.google.gwt.core.client.GWT;

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

	private static UserGroupServiceAsync userGroupService;

	public static UserGroupServiceAsync getUserGroupService() {
		if (userGroupService == null) {
			userGroupService = GWT.create(UserGroupService.class);
		}
		return userGroupService;
	}


	private static UserTabServiceAsync userTabService;

	public static UserTabServiceAsync getUserTabService() {
		if (userTabService == null) {
			userTabService = GWT.create(UserTabService.class);
		}
		return userTabService;
	}

	private static ServiceTabServiceAsync serviceTabService;

	public static ServiceTabServiceAsync getServiceTabService() {
		if (serviceTabService == null) {
			serviceTabService = GWT.create(ServiceTabService.class);
		}
		return serviceTabService;
	}

	private static ChannelServiceAsync channelService;

	public static ChannelServiceAsync getChannelService() {
		if (channelService == null) {
			channelService = GWT.create(ChannelService.class);
		}
		return channelService;
	}

	private static ClientCommissionsServiceAsync clientCommissionsService;

	public static ClientCommissionsServiceAsync getClientCommissionsService() {
		if (clientCommissionsService == null) {
			clientCommissionsService = GWT.create(ClientCommissionsService.class);
		}
		return clientCommissionsService;
	}

	private static ServiceCommissionsServiceAsync serviceCommissionsService;

	public static ServiceCommissionsServiceAsync getServiceCommissionsService() {
		if (serviceCommissionsService == null) {
			serviceCommissionsService = GWT.create(ServiceCommissionsService.class);
		}
		return serviceCommissionsService;
	}

	private static BankIntegrationServiceAsync bankIntegrationService;

	public static BankIntegrationServiceAsync getBankIntegrationService() {
		if (bankIntegrationService == null) {
			bankIntegrationService = GWT.create(BankIntegrationService.class);
		}
		return bankIntegrationService;
	}

}
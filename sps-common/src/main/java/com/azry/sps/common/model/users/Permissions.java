package com.azry.sps.common.model.users;

public enum Permissions {
	SYSTEM_PARAMETERS_VIEW("systemParameters"),
	SYSTEM_PARAMETERS_MANAGE("systemParameters"),
	USERS_VIEW("users"),
	USERS_MANAGE("users"),
	USER_GROUPS_VIEW("userGroups"),
	USER_GROUPS_MANAGE("userGroups"),
	CHANNELS_VIEW("channels"),
	CHANNELS_MANAGE("channels"),
	SERVICE_GROUPS_VIEW("serviceGroups"),
	SERVICE_GROUPS_MANAGE("serviceGroups"),
	SERVICES_VIEW("services"),
	SERVICES_MANAGE("services"),
	CLIENT_COMMISSIONS_VIEW("commissions"),
	CLIENT_COMMISSIONS_MANAGE("commissions"),
	SERVICE_COMMISSIONS_VIEW("commissions"),
	SERVICE_COMMISSIONS_MANAGE("commissions"),
	PAYMENTS_VIEW("payments"),
	PAYMENTS_PERFORM("payments");

	private final String permission;

	Permissions(String permission) {

		this.permission = permission;
	}
}

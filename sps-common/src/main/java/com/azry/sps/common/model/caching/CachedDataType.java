package com.azry.sps.common.model.caching;


import com.azry.sps.common.model.channels.Channel;
import com.azry.sps.common.model.commission.ClientCommissions;
import com.azry.sps.common.model.commission.ServiceCommissions;
import com.azry.sps.common.model.service.ServiceEntity;

public enum CachedDataType {

	SERVICES(ServiceEntity.class.getSimpleName()),
	CLIENT_COMMISSIONS(ClientCommissions.class.getSimpleName()),
	CHANNEL(Channel.class.getSimpleName()),
	SERVICE_COMMISSIONS(ServiceCommissions.class.getSimpleName());

	private final String classSimpleName;

	CachedDataType(String classSimpleName) {
		this.classSimpleName = classSimpleName;
	}

	public String getClassSimpleName() {
		return classSimpleName;
	}
}
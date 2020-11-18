package com.azry.sps.common.events;

public class UpdateCacheEvent {

	private String confClass;

	public UpdateCacheEvent(String confClass) {
		this.confClass = confClass;
	}

	public String getConfClass() {
		return confClass;
	}
}
package com.azry.sps.common.model.service;

public enum ServiceColumnNames {
	NAME("name"),
	ACTIVE("active");

	public enum ActivationStatus {
		ACTIVE("1"),
		INACTIVE("0");

		String status;

		ActivationStatus(String status) {
			this.status = status;
		}

		public String getStatus() {
			return status;
		}

	}

	String name;

	ServiceColumnNames(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}

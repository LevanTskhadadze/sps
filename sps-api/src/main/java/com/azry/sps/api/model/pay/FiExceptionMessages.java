package com.azry.sps.api.model.pay;

public enum FiExceptionMessages {
	VALIDATION("Invalid personal number"),
	ACCOUNT_NOT_FOUND("Client could not be found");

	String message;

	FiExceptionMessages(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}

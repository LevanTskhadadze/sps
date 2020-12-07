package com.azry.sps.integration.sp.exception;

public class SpIntegrationException extends Exception {

	private String status;

	public SpIntegrationException(String message) {
		super(message);
	}

	public SpIntegrationException(String message, String status) {
		super(message);
		this.status = status;
	}


}

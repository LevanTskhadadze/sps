package com.azry.sps.integration.sp.dto;

public class PayResponse {

	private SpResponseStatus status;

	private String message;

	public PayResponse() {
	}

	public PayResponse(SpResponseStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	public SpResponseStatus getStatus() {
		return status;
	}

	public void setStatus(SpResponseStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

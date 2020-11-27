package com.azry.sps.integration.sp.exception;

public class SpIntegrationException extends Exception {

	public enum Type {
		CONNECTION_FAILED("spConnectionFailed"),
		BAD_REQUEST("spBadRequest");

		private String code;

		private Type(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}

	Type type;



	public SpIntegrationException(Type type, Exception ex) {
		super(ex.getCause());
		this.type = type;
	}

	public Type getType() {
		return type;
	}
}

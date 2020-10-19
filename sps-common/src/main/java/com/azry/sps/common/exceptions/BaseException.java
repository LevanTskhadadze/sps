package com.azry.sps.common.exceptions;

public class BaseException extends Exception {

	private String code;

	private String[] params;

	protected BaseException() {}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public BaseException(String message, String code, String[] params, Throwable throwable) {
		this(message, throwable);
		this.code = code;
		this.params = params;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}
}
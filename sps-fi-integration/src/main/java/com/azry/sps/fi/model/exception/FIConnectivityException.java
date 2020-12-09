package com.azry.sps.fi.model.exception;

import lombok.Data;

import javax.ejb.ApplicationException;

@ApplicationException
@Data
public class FIConnectivityException extends Exception {


	public FIConnectivityException() {
	}

	public FIConnectivityException(String message) {
		super(message);
	}

	public FIConnectivityException(String message, Throwable throwable) {
		super(message, throwable);
	}
}

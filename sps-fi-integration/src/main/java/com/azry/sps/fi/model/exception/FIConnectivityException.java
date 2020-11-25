package com.azry.sps.fi.model.exception;

import javax.ejb.ApplicationException;

@ApplicationException
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

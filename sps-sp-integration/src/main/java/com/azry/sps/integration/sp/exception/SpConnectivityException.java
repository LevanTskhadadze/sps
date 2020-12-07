package com.azry.sps.integration.sp.exception;

import javax.ejb.ApplicationException;

@ApplicationException
public class SpConnectivityException extends Exception {

	public SpConnectivityException(String message, Exception exception) {
		super(message, exception);

	}
}
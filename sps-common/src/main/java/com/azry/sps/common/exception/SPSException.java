package com.azry.sps.common.exception;


import javax.ejb.ApplicationException;

@ApplicationException
public class SPSException extends Exception {


	public SPSException() {
	}

	public SPSException(String message) {
		super(message);
	}

	public SPSException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
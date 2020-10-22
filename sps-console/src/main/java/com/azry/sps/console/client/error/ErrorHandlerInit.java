package com.azry.sps.console.client.error;

import com.google.gwt.core.client.GWT;

public class ErrorHandlerInit {

	public static ErrorHandler errorHandler;

	public static void init() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler());
		errorHandler = new ErrorHandlerImpl();
	}
}

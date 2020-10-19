package com.azry.sps.console.client.error;


import com.google.gwt.core.client.GWT;


public class UncaughtExceptionHandler implements GWT.UncaughtExceptionHandler {

    @Override
	public void onUncaughtException(Throwable e) {
       ErrorHandlerInit.errorHandler.onError(e);
    }
}
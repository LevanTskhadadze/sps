package com.azry.sps.console.shared.clientexception;

import com.azry.sps.common.exceptions.SPSException;
import com.google.gwt.core.shared.GwtIncompatible;

public class SPSConsoleException extends Exception {


	public SPSConsoleException() {
		super();
	}
	
	public SPSConsoleException(String message, Exception ex) {
		super(message, ex);
		
	}
	
	public SPSConsoleException(String message) {
		super(message);
	}


	@GwtIncompatible
	public SPSConsoleException(SPSException spsEx) {
		super(spsEx.getMessage(), spsEx.getCause());
	}

}
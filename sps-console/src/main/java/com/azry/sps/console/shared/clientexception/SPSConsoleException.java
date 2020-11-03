package com.azry.sps.console.shared.clientexception;

import com.azry.sps.common.exceptions.SPSException;
import com.google.gwt.core.shared.GwtIncompatible;

public class SPSConsoleException extends Exception {
	
	private String messageKey;

	public SPSConsoleException() {
		super();
	}
	
	public SPSConsoleException(String messageKey, Exception ex) {
		super(ex);
		this.messageKey = messageKey;
		
	}
	
	public SPSConsoleException(String messageKey) {
		this.messageKey = messageKey;
	}


	@GwtIncompatible
	public SPSConsoleException(SPSException spsEx) {
		super(spsEx.getCause());
		this.messageKey = spsEx.getMessage();
	}

	public String getMessageKey() {
		return messageKey;
	}

}
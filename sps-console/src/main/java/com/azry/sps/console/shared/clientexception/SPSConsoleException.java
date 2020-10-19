package com.azry.sps.console.shared.clientexception;

import com.azry.sps.common.exceptions.SPSException;
import com.google.gwt.core.shared.GwtIncompatible;

public class SPSConsoleException extends Exception {
	
	private String messageKey;
	
	private String[] params;
	
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
	
	public SPSConsoleException(String messageKey, String... params) {
		this.messageKey = messageKey;
		this.params = params;
	}

	@GwtIncompatible
	public SPSConsoleException(SPSException opcEx) {
		this.messageKey = opcEx.getCode() != null ? opcEx.getCode() : opcEx.getMessage();
		this.params = opcEx.getParams();
	}

	public String getMessageKey() {
		return messageKey;
	}

	public String[] getParams() {
		return params;
	}
}
package com.azry.sps.fi.model.exception;

import com.azry.sps.fi.bankws.BankServiceException_Exception;
import lombok.Data;

import javax.ejb.ApplicationException;

@ApplicationException
@Data
public class FIException extends Exception {

	String code;

	public FIException() {}

	public FIException(String message) {
		super(message);
	}

	public FIException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public FIException(BankServiceException_Exception bankServiceException) {
		super(bankServiceException.getMessage(), bankServiceException.getCause());
		this.code = bankServiceException.getFaultInfo().getCode();
	}
}

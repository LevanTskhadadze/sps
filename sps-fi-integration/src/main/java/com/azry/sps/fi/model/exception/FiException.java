package com.azry.sps.fi.model.exception;

import com.azry.sps.fi.bankws.BankServiceException_Exception;
import lombok.Getter;

import javax.ejb.ApplicationException;

@ApplicationException
@Getter
public class FiException extends Exception {

	String code;

	public FiException() {}

	public FiException(String message) {
		super(message);
	}

	public FiException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public FiException(BankServiceException_Exception bankServiceException) {
		super(bankServiceException.getFaultInfo().getMessage(), bankServiceException.getCause());
		this.code = bankServiceException.getFaultInfo().getCode();
	}
}

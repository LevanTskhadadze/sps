package com.azry.sps.common.exceptions;


import org.apache.commons.lang3.StringUtils;

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

	public static class Builder {

		private String message;


		private Throwable throwable;

		public Builder message(String message) {
			this.message = message;
			return this;
		}


		public Builder exception(Throwable throwable) {
			this.throwable = throwable;
			return this;
		}

		public SPSException build() {
			SPSException ex;
			if (message != null && throwable != null) {
				ex = new SPSException(message, throwable);
			} else if (!StringUtils.isEmpty(message)) {
				ex = new SPSException(message);
			} else {
				ex = new SPSException();
			}
			return ex;
		}
	}
}
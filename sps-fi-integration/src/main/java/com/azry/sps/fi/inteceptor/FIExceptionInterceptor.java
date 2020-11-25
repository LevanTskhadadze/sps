package com.azry.sps.fi.inteceptor;

import com.azry.sps.fi.model.exception.FIConnectivityException;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.xml.ws.WebServiceException;

public class FIExceptionInterceptor {

	@AroundInvoke
	public Object handleException(InvocationContext context) throws Throwable {

		Object result = null;
		try {
			result = context.proceed();
		} catch (WebServiceException ex) {
			throw new FIConnectivityException();
		}
		return result;
	}
}

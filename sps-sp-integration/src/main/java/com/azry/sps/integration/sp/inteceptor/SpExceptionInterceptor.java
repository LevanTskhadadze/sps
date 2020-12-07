package com.azry.sps.integration.sp.inteceptor;

import com.azry.sps.integration.sp.exception.SpConnectivityException;
import lombok.extern.slf4j.Slf4j;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.ws.rs.ProcessingException;

@Slf4j
public class SpExceptionInterceptor {

	@AroundInvoke
	public Object handleException(InvocationContext context) throws Throwable {

		Object result = null;
		try {
			result = context.proceed();
		} catch (ProcessingException ex) {
			throw new SpConnectivityException("Service Provider Connection Failed", ex);
		}
		return result;
	}
}
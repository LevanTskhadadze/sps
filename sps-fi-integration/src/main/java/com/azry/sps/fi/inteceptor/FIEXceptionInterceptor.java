package com.azry.sps.fi.inteceptor;

import com.azry.sps.common.exceptions.SPSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.xml.ws.WebServiceException;


public class FIEXceptionInterceptor {
	private static Logger log = LoggerFactory.getLogger(FIEXceptionInterceptor.class);

	@AroundInvoke
	public Object handleException(InvocationContext context) throws Throwable {

		Object result = null;
		SPSException SPSEx = null;
		try {
			result = context.proceed();
		} catch (SPSException ex) {
			SPSEx = ex;
		} catch (WebServiceException ex) {
			SPSEx = new SPSException("bankConnectionError");
		} finally {
			if (SPSEx != null) {
				if (SPSEx.getCause() == null) {
					log.error(SPSEx.getMessage());
				} else {
					log.error(SPSEx.getMessage(), SPSEx);
				}
				throw SPSEx;
			}
		}
		return result;
	}
}

package com.azry.sps.api;

import com.azry.sps.api.model.SpsApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class SpsApiInterceptor {

	Logger log = LoggerFactory.getLogger(SpsApi.class);

	@AroundInvoke
	public Object process(InvocationContext context) throws Exception{
		long t = System.currentTimeMillis();

		String parameters = "";
		if (context.getParameters().length > 0) {
			parameters = "\nParameters: \n" + context.getParameters()[0].toString();
		}

		String methodName = context.getMethod().getName();
		log.info("Invoking SPS API method " + methodName + "." + parameters);

		Object result;


		try {
			result = context.proceed();
		} catch (Exception ex) {

			String expected = "";
			if (!(ex instanceof SpsApiException)) {
				expected = "unexpected";
			}
			log.error("method " + methodName + " threw an " + expected + " exception: " + ex.getMessage());
			throw ex;
		} finally {
			long time = (System.currentTimeMillis() - t) / 1000;
			log.info("Invocation of method " + methodName + " completed in " + time + " mils.");
		}

		return result;
	}
}

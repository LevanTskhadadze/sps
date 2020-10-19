package com.azry.sps.console.server;

import com.azry.sps.console.shared.clientexception.ClientExceptionHandlerService;
import com.google.gwt.core.shared.SerializableThrowable;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.servlet.annotation.WebServlet;


@Slf4j
@WebServlet("sps/servlet/exceptionHandlerService")
public class ClientExceptionHandlerServiceImpl extends RemoteServiceServlet implements ClientExceptionHandlerService {

    @Override
	public void onClientException(SerializableThrowable th) {
		log.error(ExceptionUtils.getStackTrace(th));
    }
}
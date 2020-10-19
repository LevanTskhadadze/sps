package com.azry.sps.console.shared.clientexception;

import com.google.gwt.core.shared.SerializableThrowable;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("servlet/exceptionHandlerService")
public interface ClientExceptionHandlerService extends RemoteService {

	void onClientException(SerializableThrowable th);
}
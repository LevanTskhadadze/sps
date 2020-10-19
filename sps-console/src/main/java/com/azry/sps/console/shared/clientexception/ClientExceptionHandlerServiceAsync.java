package com.azry.sps.console.shared.clientexception;

import com.google.gwt.core.shared.SerializableThrowable;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface  ClientExceptionHandlerServiceAsync {

	void onClientException(SerializableThrowable th, AsyncCallback<Void> async);
}
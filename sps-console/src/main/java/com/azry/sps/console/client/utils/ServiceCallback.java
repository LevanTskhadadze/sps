package com.azry.sps.console.client.utils;

import com.azry.sps.console.client.ConsoleEntryPoint;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.Component;

public abstract class ServiceCallback<T> implements AsyncCallback<T> {

	private Component comp;

	private boolean showLoading = true;

	public ServiceCallback() {
		this(ConsoleEntryPoint.VIEWPORT);
	}

	public ServiceCallback(Component comp) {
		this(comp, true);
	}

	public ServiceCallback(boolean showLoading) {
		this(null, showLoading);
	}

	private ServiceCallback(Component comp, boolean showLoading) {
		this.comp = comp;
		this.showLoading = showLoading;
		if (showLoading) {
			Overlay.startLoading(comp);
		}
	}

	@Override
	public void onFailure(Throwable th) {
		if (showLoading) {
            Overlay.stopLoading(comp);
        }
        onServiceFailure(th);
	}

    public void onServiceFailure(Throwable th) {
	    // TODO: 12/20/2018 handle service failure
    }

	@Override
	public void onSuccess(T result) {
		if (showLoading) {
            Overlay.stopLoading(comp);
        }
		onServiceSuccess(result);
	}

	public abstract void onServiceSuccess(T result);
}
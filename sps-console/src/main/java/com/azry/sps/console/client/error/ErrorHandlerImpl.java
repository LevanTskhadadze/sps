package com.azry.sps.console.client.error;

import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.clientexception.ClientExceptionHandlerService;
import com.azry.sps.console.shared.clientexception.ClientExceptionHandlerServiceAsync;
import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.shared.SerializableThrowable;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;


public class ErrorHandlerImpl implements ErrorHandler {

	private static final ClientExceptionHandlerServiceAsync EXCEPTION_HANDLER = GWT.create(ClientExceptionHandlerService.class);

	private boolean unexpectedErrorMessageIsActive;

	@Override
	public void onError(Throwable th) {
		if (th instanceof SPSConsoleException) {
			GWT.log("Console exception", th);
			SPSConsoleException ve = (SPSConsoleException) th;
			sendToServer(th);
			showValidationInfo(Mes.get(ve.getMessageKey(), ve.getParams()));
		} else {
			if (th instanceof StatusCodeException) {
				int statusCode = ((StatusCodeException) th).getStatusCode();
				GWT.log("Exception statusCode = " + statusCode, th);
				if (statusCode == 440 || statusCode == 403) {
					showLogOutWindow();
				} else if (statusCode == 0) {
					showValidationInfo(Mes.get("serverConnectWarningOccurred"));
				} else {
					sendToServer(th);
					showErrorInfo(Mes.get("unexpectedErrorOccurred"));
				}
			} else {
				GWT.log("Unexpected exception", th);
				sendToServer(th);
				showErrorInfo(Mes.get("unexpectedErrorOccurred"));
			}
		}
	}

	private void showLogOutWindow() {
		MessageBox dialog = new MessageBox(Mes.get("message"), Mes.get("sessionTimeOut"));
		dialog.setIcon(MessageBox.ICONS.info());
		dialog.getButton(Dialog.PredefinedButton.OK).setText(Mes.get("login"));
		dialog.addDialogHideHandler(new DialogHideEvent.DialogHideHandler() {
			@Override
			public void onDialogHide(DialogHideEvent event) {
				if (event.getHideButton() == Dialog.PredefinedButton.OK) {
					Window.Location.replace(GWT.getHostPageBaseURL());
				}
			}
		});
		dialog.show();
	}

	private void showErrorInfo(String info) {
		showInfo(MessageBox.ICONS.error(), info, null, true);
	}

	private void showValidationInfo(String info) {
		showInfo(MessageBox.ICONS.warning(), info, null, false);
	}

	private void showInfo(ImageResource icon, String info, Integer width, final boolean error) {
		MessageBox box = new MessageBox(Mes.get("message"));
		box.setPredefinedButtons(Dialog.PredefinedButton.OK);
		box.setIcon(icon);
		box.setMessage(info);
		if (width != null) {
			box.setMinWidth(width);
		}
		if (!error || !unexpectedErrorMessageIsActive) {
			box.show();
		}
		box.addHideHandler(new HideEvent.HideHandler() {
			@Override
			public void onHide(HideEvent event) {
				if (error) {
					unexpectedErrorMessageIsActive = false;
				}
			}
		});
		if (error) {
			unexpectedErrorMessageIsActive = true;
		}
	}

	private void sendToServer(Throwable th) {

		EXCEPTION_HANDLER.onClientException(SerializableThrowable.fromThrowable(th), new ServiceCallback<Void>() {
			@Override
			public void onServiceSuccess(Void result) {
			}

			@Override
			public void onFailure(Throwable th) {
			}
		});
	}

}
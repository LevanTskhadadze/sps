package com.azry.sps.console.client.utils;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.MessageBox;

public class DialogUtils {

	public static void showInfo(String message) {
		showDialog(MessageBox.ICONS.info(), message, null);
	}

	public static void showInfo(String message, Integer width) {
		showDialog(MessageBox.ICONS.info(), message, width);
	}

	public static void showWarning(String message) {
		showDialog(MessageBox.ICONS.warning(), message, null);
	}

	public static void showWarning(String message, Integer width) {
		showDialog(MessageBox.ICONS.warning(), message, width);
	}

	private static void showDialog(ImageResource icon, String message, Integer width) {
		MessageBox box = new MessageBox(Mes.get("message"));
		box.setPredefinedButtons(Dialog.PredefinedButton.OK);
		box.setIcon(icon);
		box.setMessage(message);
		if (width != null) {
			box.setMinWidth(width);
		}
		box.show();
	}
}
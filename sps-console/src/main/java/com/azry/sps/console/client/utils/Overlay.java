package com.azry.sps.console.client.utils;

import com.sencha.gxt.widget.core.client.Component;

public class Overlay {

	public static void startLoading(Component comp) {
		if (comp != null) {
			comp.mask(Mes.get("loading"));
		}
	}

	public static void stopLoading(Component comp) {
		if (comp != null) {
			comp.unmask();
		}
	}
}
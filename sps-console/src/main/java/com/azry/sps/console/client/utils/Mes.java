package com.azry.sps.console.client.utils;

import com.azry.sps.console.client.Messages;
import com.google.gwt.core.client.GWT;
import com.sencha.gxt.core.client.util.Format;

import java.util.MissingResourceException;

public class Mes {

	private static Messages messages;

	public static String get(String key) {
		if (messages == null) {
			messages = GWT.create(Messages.class);
		}

		String value = key;
		if (messages != null) {
			try {
				value = messages.getString(key);
			} catch(MissingResourceException ex) {
				GWT.log("Missing resource: " + key, ex);
				value = "Missing: " + key;
			}
		}

		return value;
	}

	public static String get(String key, String... params) {
		if (params == null || params.length == 0) {
			return get(key);
		}
		return Format.substitute(get(key), (Object[]) params);
	}
}
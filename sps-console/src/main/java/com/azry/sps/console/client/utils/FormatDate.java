package com.azry.sps.console.client.utils;


import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Date;

public class FormatDate {

	private static final String DATETIME_PATTERN = "dd-MM-yyyy HH:mm:ss";
	private static final String DATE_PATTERN = "dd/MM/yyyy";


	public static String formatDateTime(Date date) {

		return date == null ? "" : DateTimeFormat.getFormat(DATETIME_PATTERN).format(date);
	}

	public static String formatDate(Date date) {

		return date == null ? "" : DateTimeFormat.getFormat(DATE_PATTERN).format(date);
	}
}

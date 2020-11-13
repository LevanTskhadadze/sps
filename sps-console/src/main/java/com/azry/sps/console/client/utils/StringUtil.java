package com.azry.sps.console.client.utils;

import java.util.List;

public class StringUtil {

	public static String join(List<String> list, String del) {

		StringBuilder sb = new StringBuilder();

		String Del = "";

		for(String s : list) {

			sb.append(Del);
			sb.append(s);

			Del = del;
		}

		return sb.toString();
	}

	public static String joinEndWithDel(List<String> list, String del) {

		StringBuilder sb = new StringBuilder();

		for(String s : list) {

			sb.append(s);
			sb.append(del);
		}

		return sb.toString();
	}

	public static String joinMes(List<String> list, String del) {

		StringBuilder sb = new StringBuilder();

		String Del = "";

		for(String s : list) {

			sb.append(Del);
			sb.append(Mes.get(s));

			Del = del;
		}

		return sb.toString();
	}
}

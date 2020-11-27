package com.azry.sps.api.dto;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "GetInfoStatus")
public enum GetInfoStatus {
	BAD_REQUEST,
	SUCCESS,
	ABONENT_NOT_FOUND
}

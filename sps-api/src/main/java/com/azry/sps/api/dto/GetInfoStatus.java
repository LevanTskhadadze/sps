package com.azry.sps.api.dto;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "GetInfoStatus")
public enum GetInfoStatus {
	BAD_REQUEST,
	CONNECTION_FAILED,
	SUCCESS,
	ABONENT_NOT_FOUND,
	SERVICE_NOT_FOUND
}

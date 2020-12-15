package com.azry.sps.api.model.pay;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "PaymentStatus")
public enum PaymentStatusDTO {
	CREATED,
	COLLECT_PENDING,
	COLLECTED,
	COLLECT_REJECTED,
	PENDING,
	PERFORMED,
	REJECTED
}

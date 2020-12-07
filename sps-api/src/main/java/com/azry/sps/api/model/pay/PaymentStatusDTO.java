package com.azry.sps.api.model.pay;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "PaymentStatusDTO")
public enum PaymentStatusDTO {
	CREATED,
	COLLECT_PENDING,
	COLLECTED,
	COLLECT_REJECTED,
	PENDING,
	PERFORMED,
	REJECTED
}

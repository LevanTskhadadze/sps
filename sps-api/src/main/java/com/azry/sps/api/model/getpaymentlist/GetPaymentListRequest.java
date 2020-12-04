package com.azry.sps.api.model.getpaymentlist;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "GetPaymentListRequest", propOrder = {"personalNumber"})
public class GetPaymentListRequest implements Serializable {

	String personalNumber;

	@XmlElement(name = "personalNumber", required = true)
	public String getPersonalNumber() {
		return personalNumber;
	}

	public void setPersonalNumber(String personalNumber) {
		this.personalNumber = personalNumber;
	}
}

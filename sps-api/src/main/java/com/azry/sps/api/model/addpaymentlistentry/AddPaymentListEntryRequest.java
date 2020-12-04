package com.azry.sps.api.model.addpaymentlistentry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "AddPaymentListEntryRequest", propOrder = {"personalNumber", "paymentList"})
public class AddPaymentListEntryRequest implements Serializable {

	String personalNumber;

	PaymentListDTO paymentList;

	@XmlElement(name = "personalNumber", required = true)
	public String getPersonalNumber() {
		return personalNumber;
	}

	public void setPersonalNumber(String personalNumber) {
		this.personalNumber = personalNumber;
	}

	@XmlElement(name = "paymentList", required = true)
	public PaymentListDTO getPaymentList() {
		return paymentList;
	}

	public void setPaymentList(PaymentListDTO paymentList) {
		this.paymentList = paymentList;
	}
}

package com.azry.sps.api.model.addpaymentlistentry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "AddPaymentListEntryRequest", propOrder = {"personalNumber", "paymentListEntry"})
public class AddPaymentListEntryRequest implements Serializable {

	String personalNumber;

	PaymentListEntryDTO paymentListEntry;

	@XmlElement(name = "personalNumber")
	public String getPersonalNumber() {
		return personalNumber;
	}

	public void setPersonalNumber(String personalNumber) {
		this.personalNumber = personalNumber;
	}

	@XmlElement(name = "paymentListEntry")
	public PaymentListEntryDTO getPaymentListEntry() {
		return paymentListEntry;
	}

	public void setPaymentListEntry(PaymentListEntryDTO paymentListEntry) {
		this.paymentListEntry = paymentListEntry;
	}

	public boolean isValid() {
		return personalNumber != null && paymentListEntry != null;
	}
}

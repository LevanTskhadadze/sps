package com.azry.sps.api.model.getpaymentlist;

import com.azry.sps.api.model.addpaymentlistentry.PaymentListEntryDTO;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

@XmlType(name = "GetPaymentListResponse", propOrder = {"paymentLists"})
public class GetPaymentListResponse implements Serializable {

	List<PaymentListEntryDTO> paymentLists;

	@XmlElement(name = "paymentLists")
	public List<PaymentListEntryDTO> getPaymentLists() {
		return paymentLists;
	}

	public void setPaymentLists(List<PaymentListEntryDTO> paymentLists) {
		this.paymentLists = paymentLists;
	}
}

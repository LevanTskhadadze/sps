package com.azry.sps.api.model.getpaymentlist;

import com.azry.sps.api.model.addpaymentlistentry.PaymentListDTO;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

@XmlType(name = "GetPaymentListResponse", propOrder = {"paymentLists"})
public class GetPaymentListResponse implements Serializable {

	List<PaymentListDTO> paymentLists;

	@XmlElement(name = "paymentLists")
	public List<PaymentListDTO> getPaymentLists() {
		return paymentLists;
	}

	public void setPaymentLists(List<PaymentListDTO> paymentLists) {
		this.paymentLists = paymentLists;
	}
}

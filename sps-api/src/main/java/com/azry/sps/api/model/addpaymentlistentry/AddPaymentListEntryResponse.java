package com.azry.sps.api.model.addpaymentlistentry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "AddPaymentListEntryResponse", propOrder = {"id"})
public class AddPaymentListEntryResponse implements Serializable {

	long id;

	@XmlElement(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}

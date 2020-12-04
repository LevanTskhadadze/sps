package com.azry.sps.api.model.removepaymentlistentry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "RemovePaymentListEntryRequest", propOrder = {"id"})
public class RemovePaymentListEntryRequest implements Serializable {

	long id;

	@XmlElement(name = "id", required = true)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}

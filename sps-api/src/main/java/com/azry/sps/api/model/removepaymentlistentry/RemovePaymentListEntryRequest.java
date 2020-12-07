package com.azry.sps.api.model.removepaymentlistentry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "RemovePaymentListEntryRequest", propOrder = {"id"})
public class RemovePaymentListEntryRequest implements Serializable {

	Long id;

	@XmlElement(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isValid() {
		return id != null;
	}
}

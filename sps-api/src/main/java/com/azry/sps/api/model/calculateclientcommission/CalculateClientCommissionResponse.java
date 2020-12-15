package com.azry.sps.api.model.calculateclientcommission;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.math.BigDecimal;

@XmlType(name = "CalculateClientCommissionResponse", propOrder = {"clientCommission"})
public class CalculateClientCommissionResponse implements Serializable {

	BigDecimal clientCommission;

	@XmlElement(name = "clientCommission", required = true)
	public BigDecimal getClientCommission() {
		return clientCommission;
	}

	public void setClientCommission(BigDecimal clientCommission) {
		this.clientCommission = clientCommission;
	}
}

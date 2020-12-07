package com.azry.sps.api.model.getinfo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.math.BigDecimal;

@XmlType(name = "GetInfoResponse", propOrder = {"infoMessage", "debt"})
public class GetInfoResponse implements Serializable {

	private String infoMessage;

	private BigDecimal debt;


	@XmlElement(name = "debt")
	public BigDecimal getDebt() {
		return debt;
	}

	@XmlElement(name = "infoMessage")
	public String getInfoMessage() {
		return infoMessage;
	}


	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}

	public void setDebt(BigDecimal debt) {
		this.debt = debt;
	}
}

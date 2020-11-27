package com.azry.sps.api.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;

@XmlType(name = "GetInfoResponse", propOrder = {"status", "statusMessage", "infoMessage", "debt"})
public class GetInfoResponse {

	private GetInfoStatus status;

	private String statusMessage;

	private String infoMessage;

	private BigDecimal debt;

	@XmlElement(name = "status")
	public GetInfoStatus getStatus() {
		return status;
	}

	@XmlElement(name = "statusMessage")
	public String getStatusMessage() {
		return statusMessage;
	}

	@XmlElement(name = "debt")
	public BigDecimal getDebt() {
		return debt;
	}

	@XmlElement
	public String getInfoMessage() {
		return infoMessage;
	}


	public void setStatus(GetInfoStatus status) {
		this.status = status;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}

	public void setDebt(BigDecimal debt) {
		this.debt = debt;
	}
}

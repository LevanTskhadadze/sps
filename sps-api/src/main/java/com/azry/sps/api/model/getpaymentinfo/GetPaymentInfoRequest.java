package com.azry.sps.api.model.getpaymentinfo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "GetPaymentInfoRequest", propOrder = {"agentPaymentId"})
public class GetPaymentInfoRequest implements Serializable {

	String agentPaymentId;

	@XmlElement(name = "paymentId", required = true)
	public String getAgentPaymentId() {
		return agentPaymentId;
	}

	public void setAgentPaymentId(String agentPaymentId) {
		this.agentPaymentId = agentPaymentId;
	}

	public boolean isValid() {
		return agentPaymentId != null;
	}

	@Override
	public String toString() {
		return "Agent payment ID: " + agentPaymentId;
	}
}

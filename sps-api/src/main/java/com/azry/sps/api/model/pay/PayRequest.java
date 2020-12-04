package com.azry.sps.api.model.pay;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.math.BigDecimal;

@XmlType(name = "PayRequest", propOrder = {"agentPaymentId", "serviceId", "channelId", "abonentCode", "personalNumber", "amount"})
public class PayRequest implements Serializable {

	String agentPaymentId;

	String abonentCode;

	String personalNumber;

	BigDecimal amount;

	long serviceId;

	Long channelId;

	@XmlElement(name = "personalNumber", required = true)
	public String getPersonalNumber() {
		return personalNumber;
	}

	public void setPersonalNumber(String personalNumber) {
		this.personalNumber = personalNumber;
	}

	@XmlElement(name = "amount", required = true)
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@XmlElement(name = "serviceId", required = true)
	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	@XmlElement(name = "abonentCode", required = true)
	public String getAbonentCode() {
		return abonentCode;
	}

	public void setAbonentCode(String abonentCode) {
		this.abonentCode = abonentCode;
	}


	@XmlElement(name = "agentPaymentId", required = true)
	public String getAgentPaymentId() {
		return agentPaymentId;
	}

	public void setAgentPaymentId(String personalNumber) {
		this.agentPaymentId = personalNumber;
	}

	@XmlElement(name = "channelId", required = true)
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
}

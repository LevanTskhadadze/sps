package com.azry.sps.api.model.pay;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.math.BigDecimal;

@XmlType(name = "PayRequest", propOrder = {"agentPaymentId", "serviceId", "channelId", "abonentCode", "personalNumber", "amount", "clientAccountBAN", "purpose"})
public class PayRequest implements Serializable {

	private String agentPaymentId;

	private String abonentCode;

	private String personalNumber;

	private BigDecimal amount;

	private Long serviceId;

	private Long channelId;

	private String clientAccountBAN;

	private String purpose;

	@XmlElement(name = "personalNumber")
	public String getPersonalNumber() {
		return personalNumber;
	}

	public void setPersonalNumber(String personalNumber) {
		this.personalNumber = personalNumber;
	}

	@XmlElement(name = "amount")
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@XmlElement(name = "serviceId")
	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	@XmlElement(name = "abonentCode")
	public String getAbonentCode() {
		return abonentCode;
	}

	public void setAbonentCode(String abonentCode) {
		this.abonentCode = abonentCode;
	}

	@XmlElement(name = "agentPaymentId")
	public String getAgentPaymentId() {
		return agentPaymentId;
	}

	public void setAgentPaymentId(String personalNumber) {
		this.agentPaymentId = personalNumber;
	}

	@XmlElement(name = "channelId")
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	@XmlElement(name = "clientAccountBAN")
	public String getClientAccountBAN() {
		return clientAccountBAN;
	}

	public void setClientAccountBAN(String clientAccountBAN) {
		this.clientAccountBAN = clientAccountBAN;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public boolean isValid() {
		return agentPaymentId != null
			&& abonentCode != null
			&& personalNumber != null
			&& amount != null
			&& serviceId != null
			&& channelId != null
			&& clientAccountBAN != null;
	}
}

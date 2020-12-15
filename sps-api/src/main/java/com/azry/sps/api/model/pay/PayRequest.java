package com.azry.sps.api.model.pay;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.math.BigDecimal;

@XmlType(name = "PayRequest", propOrder = {"agentPaymentId", "serviceId", "channelId", "abonentCode", "personalNumber", "amount", "clientAccountBAN", "clientCommission"})
public class PayRequest implements Serializable {

	private String agentPaymentId;

	private String abonentCode;

	private String personalNumber;

	private BigDecimal amount;

	private Long serviceId;

	private Long channelId;

	private String clientAccountBAN;

	private BigDecimal clientCommission;

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
	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
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

	@XmlElement(name = "clientAccountBAN", required = true)
	public String getClientAccountBAN() {
		return clientAccountBAN;
	}

	public void setClientAccountBAN(String clientAccountBAN) {
		this.clientAccountBAN = clientAccountBAN;
	}

	@XmlElement(name = "clientCommission")
	public BigDecimal getClientCommission() {
		return clientCommission;
	}

	public void setClientCommission(BigDecimal clientCommission) {
		this.clientCommission = clientCommission;
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

	@Override
	public String toString() {
		return 	"Agent payment ID: " + agentPaymentId +
				"\nAbonent code: " + abonentCode +
				"\nPersonal number: " + personalNumber +
				"\nAmount: " + amount +
				"\nService ID: " + serviceId +
				"\nChannel ID: " + channelId +
				"\nClient account BAN: " + clientAccountBAN +
				"\nClient commission: " + clientCommission;
	}
}

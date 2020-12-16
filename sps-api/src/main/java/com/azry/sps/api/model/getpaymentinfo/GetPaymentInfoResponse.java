package com.azry.sps.api.model.getpaymentinfo;

import com.azry.sps.api.model.pay.PaymentStatusDTO;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.math.BigDecimal;

@XmlType(name = "GetPaymentInfoResponse", propOrder = {"agentPaymentId", "serviceId", "channelId", "abonentCode", "personalNumber", "amount", "clCommission", "svcCommission", "status", "statusMessage"})
public class GetPaymentInfoResponse implements Serializable {

	private String agentPaymentId;

	private long serviceId;

	private long channelId;

	private String abonentCode;

	private String personalNumber;

	private BigDecimal amount;

	private BigDecimal clCommission;

	private BigDecimal svcCommission;

	private PaymentStatusDTO status;

	private String statusMessage;

	@XmlElement(name = "paymentId")
	public String getAgentPaymentId() {
		return agentPaymentId;
	}

	public void setAgentPaymentId(String agentPaymentId) {
		this.agentPaymentId = agentPaymentId;
	}

	@XmlElement(name = "serviceId")
	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	@XmlElement(name = "channelId")
	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	@XmlElement(name = "abonentCode")
	public String getAbonentCode() {
		return abonentCode;
	}

	public void setAbonentCode(String abonentCode) {
		this.abonentCode = abonentCode;
	}

	@XmlElement(name = "amount")
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@XmlElement(name = "clCommission")
	public BigDecimal getClCommission() {
		return clCommission;
	}

	public void setClCommission(BigDecimal clCommission) {
		this.clCommission = clCommission;
	}

	@XmlElement(name = "clientPersonalNumber")
	public String getPersonalNumber() {
		return personalNumber;
	}

	public void setPersonalNumber(String personalNumber) {
		this.personalNumber = personalNumber;
	}

	@XmlElement(name = "svcCommission")
	public BigDecimal getSvcCommission() {
		return svcCommission;
	}

	public void setSvcCommission(BigDecimal svcCommission) {
		this.svcCommission = svcCommission;
	}

	@XmlElement(name = "status")
	public PaymentStatusDTO getStatus() {
		return status;
	}

	public void setStatus(PaymentStatusDTO status) {
		this.status = status;
	}

	@XmlElement(name = "statusMessage")
	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
}

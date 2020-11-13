package com.azry.sps.integration.sp.dto;

import java.math.BigDecimal;

public class  PaymentDto {

	private String serviceCode;

	private String agentPaymentId;

	private String abonentCode;

	private BigDecimal amount;

	public PaymentDto(String serviceCode, String agentPaymentId, String abonentCode, BigDecimal amount) {
		this.serviceCode = serviceCode;
		this.agentPaymentId = agentPaymentId;
		this.abonentCode = abonentCode;
		this.amount = amount;
	}

	public PaymentDto() {

	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getAgentPaymentId() {
		return agentPaymentId;
	}

	public void setAgentPaymentId(String agentPaymentId) {
		this.agentPaymentId = agentPaymentId;
	}

	public String getAbonentCode() {
		return abonentCode;
	}

	public void setAbonentCode(String abonentCode) {
		this.abonentCode = abonentCode;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}

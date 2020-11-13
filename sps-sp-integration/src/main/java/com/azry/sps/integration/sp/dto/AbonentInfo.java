package com.azry.sps.integration.sp.dto;


import java.math.BigDecimal;

public class AbonentInfo {

	private GetInfoStatus status;

	private String message;

	String abonentInfo;

	BigDecimal debt;

	public String getAbonentInfo() {
		return abonentInfo;
	}

	public void setAbonentInfo(String abonentInfo) {
		this.abonentInfo = abonentInfo;
	}

	public BigDecimal getDebt() {
		return debt;
	}

	public void setDebt(BigDecimal debt) {
		this.debt = debt;
	}

	public GetInfoStatus getStatus() {
		return status;
	}

	public void setStatus(GetInfoStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

package com.azry.sps.integration.sp.dto;


import java.math.BigDecimal;

public class AbonentInfoDto {

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
}

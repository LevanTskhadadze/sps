package com.azry.sps.console.shared.dto.providerintegration;


import com.azry.sps.integration.sp.dto.AbonentInfo;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

public class AbonentInfoDTO implements IsSerializable {

	private GetInfoStatusDTO status;

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

	public GetInfoStatusDTO getStatus() {
		return status;
	}

	public void setStatus(GetInfoStatusDTO status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@GwtIncompatible
	public static AbonentInfoDTO toDTO(AbonentInfo abonentInfo) {
		if (abonentInfo != null) {
			AbonentInfoDTO dto = new AbonentInfoDTO();
			dto.setStatus(GetInfoStatusDTO.valueOf(abonentInfo.getStatus().name()));
			dto.setMessage(abonentInfo.getMessage());
			dto.setAbonentInfo(abonentInfo.getAbonentInfo());
			dto.setDebt(abonentInfo.getDebt());

			return dto;
		}
		return null;
	}
}

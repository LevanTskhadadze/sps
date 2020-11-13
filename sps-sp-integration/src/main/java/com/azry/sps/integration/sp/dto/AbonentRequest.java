package com.azry.sps.integration.sp.dto;


public class AbonentRequest {
	String serviceCode;
	String abonentCode;

	public AbonentRequest(String gate, String id) {
		this.serviceCode = gate;
		this.abonentCode = id;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getAbonentCode() {
		return abonentCode;
	}

	public void setAbonentCode(String abonentCode) {
		this.abonentCode = abonentCode;
	}
}

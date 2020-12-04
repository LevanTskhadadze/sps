package com.azry.sps.api.model.getinfo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "GetInfoRequest", propOrder = {"abonentCode", "serviceId"})
public class GetInfoRequest implements Serializable {

	private String abonentCode;

	private long serviceId;

	@XmlElement(name = "abonentCode", required = true)
	public String getAbonentCode() {
		return abonentCode;
	}

	public void setAbonentCode(String abonentCode) {
		this.abonentCode = abonentCode;
	}

	@XmlElement(name = "serviceId", required = true)
	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}
}

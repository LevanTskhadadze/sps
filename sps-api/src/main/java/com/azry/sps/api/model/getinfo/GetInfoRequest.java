package com.azry.sps.api.model.getinfo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "GetInfoRequest", propOrder = {"abonentCode", "serviceId"})
public class GetInfoRequest implements Serializable {

	private String abonentCode;

	private Long serviceId;

	@XmlElement(name = "abonentCode")
	public String getAbonentCode() {
		return abonentCode;
	}

	public void setAbonentCode(String abonentCode) {
		this.abonentCode = abonentCode;
	}

	@XmlElement(name = "serviceId")
	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public boolean isValid() {
		return abonentCode != null && serviceId != null;
	}
}

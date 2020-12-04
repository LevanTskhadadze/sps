package com.azry.sps.api.model.getServices;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

@XmlType(name = "GetServicesResponse", propOrder = {"services"})
public class GetServicesResponse implements Serializable {

	List<ServiceDTO> services;

	@XmlElement(name = "services")
	public List<ServiceDTO> getServices() {
		return services;
	}

	public void setServices(List<ServiceDTO> services) {
		this.services = services;
	}
}

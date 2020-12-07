package com.azry.sps.api.model.getservicegroups;

import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

@XmlType(name = "GetServiceGroupsResponse", propOrder = {"serviceGroups"})
public class GetServiceGroupsResponse implements Serializable {

	private List<ServiceGroupDTO> serviceGroups;

	public List<ServiceGroupDTO> getServiceGroups() {
		return serviceGroups;
	}

	public void setServiceGroups(List<ServiceGroupDTO> serviceGroups) {
		this.serviceGroups = serviceGroups;
	}
}

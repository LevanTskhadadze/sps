package com.azry.sps.api.model.getservicegroups;

import com.azry.sps.common.model.groups.ServiceGroup;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlType(name = "ServiceGroup", propOrder = {"id", "name"})
public class ServiceGroupDTO {

	private Long id;

	private String name;

	@XmlElement(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public static ServiceGroupDTO entityToInfo(ServiceGroup entity) {
		ServiceGroupDTO info = new ServiceGroupDTO();

		info.setId(entity.getId());
		info.setName(entity.getName());

		return info;
	}

	public static List<ServiceGroupDTO> entitiesToInfos(List<ServiceGroup> serviceGroups) {
		List<ServiceGroupDTO> infos = new ArrayList<>();

		for (ServiceGroup serviceGroup : serviceGroups) {
			infos.add(entityToInfo(serviceGroup));
		}
		return infos;
	}

}

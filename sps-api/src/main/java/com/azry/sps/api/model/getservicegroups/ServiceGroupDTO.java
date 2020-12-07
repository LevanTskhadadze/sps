package com.azry.sps.api.model.getservicegroups;

import com.azry.sps.common.model.groups.ServiceGroup;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlType(name = "ServiceGroupDTO", propOrder = {"id", "name", "priority"})
public class ServiceGroupDTO {

	private long id;

	private String name;

	private long priority;

	@XmlElement(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "priority")
	public long getPriority() {
		return priority;
	}

	public void setPriority(long priority) {
		this.priority = priority;
	}

	public static ServiceGroupDTO entityToInfo(ServiceGroup entity) {
		ServiceGroupDTO info = new ServiceGroupDTO();

		info.setId(entity.getId());
		info.setName(entity.getName());
		info.setPriority(entity.getPriority());

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

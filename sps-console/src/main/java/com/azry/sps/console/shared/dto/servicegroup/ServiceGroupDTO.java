package com.azry.sps.console.shared.dto.servicegroup;

import com.azry.sps.common.model.groups.ServiceGroup;
import com.azry.sps.console.shared.dto.ConfigurableDTO;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;


public class ServiceGroupDTO extends ConfigurableDTO implements IsSerializable {

	private long id;

	private String name;

	private long priority;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPriority() {
		return priority;
	}

	public void setPriority(long priority) {
		this.priority = priority;
	}

	@GwtIncompatible
	public static ServiceGroupDTO toDTO(ServiceGroup serviceGroup) {
		if (serviceGroup != null) {
			ServiceGroupDTO dto = new ServiceGroupDTO();
			dto.setId(serviceGroup.getId());
			dto.setName(serviceGroup.getName());
			dto.setPriority(serviceGroup.getPriority());
			dto.setCreateTime(serviceGroup.getCreateTime());
			dto.setLastUpdateTime(serviceGroup.getLastUpdateTime());
			dto.setVersion(serviceGroup.getVersion());

			return dto;
		}
		return null;
	}

	@GwtIncompatible
	public static List<ServiceGroupDTO> toDTOs(List<ServiceGroup> serviceGroups) {
		if (serviceGroups != null) {
			List<ServiceGroupDTO> dtos = new ArrayList<>();

			for (ServiceGroup serviceGroup : serviceGroups) {
				dtos.add(toDTO(serviceGroup));

			}
			return dtos;
		}
		return null;
	}

	@GwtIncompatible
	public ServiceGroup fromDTO() {

		ServiceGroup serviceGroup = new ServiceGroup();
		serviceGroup.setId(this.getId());
		serviceGroup.setName(this.getName());
		serviceGroup.setPriority(this.getPriority());
		serviceGroup.setCreateTime(this.getCreateTime());
		serviceGroup.setLastUpdateTime(this.getLastUpdateTime());
		serviceGroup.setVersion(this.getVersion());

		return serviceGroup;

	}

}

package com.azry.sps.console.shared.dto.services;

import com.azry.sps.common.model.service.ServiceEntity;
import com.azry.sps.console.shared.dto.ConfigurableDTO;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ServiceEntityDTO extends ConfigurableDTO implements IsSerializable {

	private long id;

	private String name;

	private boolean active;

	private String data;

	public ServiceEntityDTO() {
	}

	public ServiceEntityDTO(long id, String name) {
		this.id = id;
		this.name = name;
	}

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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@GwtIncompatible
	public static ServiceEntityDTO toDto(ServiceEntity service) {
		ServiceEntityDTO dto = new ServiceEntityDTO();

		dto.setId(service.getId());
		dto.setName(service.getName());
		dto.setActive(service.isActive());
		dto.setData(service.getData());
		dto.setVersion(service.getVersion());
		dto.setCreateTime(service.getCreateTime());
		dto.setLastUpdateTime(service.getLastUpdateTime());

		return dto;
	}

	@GwtIncompatible
	public static List<ServiceEntityDTO> toDtos(List<ServiceEntity> services) {
		List<ServiceEntityDTO> dtos = new ArrayList<>();

		for(ServiceEntity service : services) {
			dtos.add(toDto(service));
		}

		return dtos;
	}
}

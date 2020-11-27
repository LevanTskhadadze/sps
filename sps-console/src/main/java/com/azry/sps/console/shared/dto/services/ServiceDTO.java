package com.azry.sps.console.shared.dto.services;


import com.azry.sps.common.model.service.Service;
import com.azry.sps.console.shared.dto.ConfigurableDTO;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ServiceDTO extends ConfigurableDTO implements IsSerializable {

	private long id;

	private String name;

	private String icon;

	private long groupId;

	private boolean allChannels;

	private List<ServiceChannelInfoDTO> channels;

	private BigDecimal minAmount;

	private BigDecimal maxAmount;

	private String serviceDebtCode;

	private String servicePayCode;

	private String providerAccountIBAN;

	private boolean active;

	public ServiceDTO(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public ServiceDTO() {
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public boolean isAllChannels() {
		return allChannels;
	}

	public void setAllChannels(boolean allChannels) {
		this.allChannels = allChannels;
	}

	public List<ServiceChannelInfoDTO> getChannels() {
		return channels;
	}

	public void setChannels(List<ServiceChannelInfoDTO> channels) {
		this.channels = channels;
	}

	public BigDecimal getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}

	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}

	public String getServiceDebtCode() {
		return serviceDebtCode;
	}

	public void setServiceDebtCode(String serviceDebtCode) {
		this.serviceDebtCode = serviceDebtCode;
	}

	public String getServicePayCode() {
		return servicePayCode;
	}

	public void setServicePayCode(String servicePayCode) {
		this.servicePayCode = servicePayCode;
	}

	public String getProviderAccountIBAN() {
		return providerAccountIBAN;
	}

	public void setProviderAccountIBAN(String providerAccountIBAN) {
		this.providerAccountIBAN = providerAccountIBAN;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@GwtIncompatible
	public static ServiceDTO toDTO(Service service) {
		if (service != null) {
			ServiceDTO dto = new ServiceDTO();
 			dto.setId(service.getId());
			dto.setName(service.getName());
			dto.setIcon(service.getIcon());
			dto.setGroupId(service.getGroupId());
			dto.setAllChannels(service.isAllChannels());
			dto.setChannels(ServiceChannelInfoDTO.toDtos(service.getChannels()));
			dto.setMinAmount(service.getMinAmount());
			dto.setMaxAmount(service.getMaxAmount());
			dto.setServiceDebtCode(service.getServiceDebtCode());
			dto.setServicePayCode(service.getServicePayCode());
			dto.setProviderAccountIBAN(service.getProviderAccountIBAN());
			dto.setActive(service.isActive());
			dto.setVersion(service.getVersion());
			dto.setCreateTime(service.getCreateTime());
			dto.setLastUpdateTime(service.getLastUpdateTime());

			return dto;
		}
		return null;
	}

	@GwtIncompatible
	public static List<ServiceDTO> toDTOs(List<Service> services) {
		List<ServiceDTO> dtos = new ArrayList<>();

		for(Service service : services) {
			dtos.add(toDTO(service));
		}

		return dtos;
	}

	@GwtIncompatible
	public static Service toEntity(ServiceDTO serviceDTO) {
		Service entity = new Service();
		if(serviceDTO == null) return entity;

		entity.setId(serviceDTO.getId());
		entity.setName(serviceDTO.getName());
		entity.setIcon(serviceDTO.getIcon());
		entity.setGroupId(serviceDTO.getGroupId());
		entity.setAllChannels(serviceDTO.isAllChannels());
		entity.setChannels(ServiceChannelInfoDTO.toEntities(serviceDTO.getChannels()));
		entity.setProviderAccountIBAN(serviceDTO.getProviderAccountIBAN());
		entity.setMinAmount(serviceDTO.getMinAmount());
		entity.setMaxAmount(serviceDTO.getMaxAmount());
		entity.setServiceDebtCode(serviceDTO.getServiceDebtCode());
		entity.setServicePayCode(serviceDTO.getServicePayCode());
		entity.setActive(serviceDTO.isActive());
		entity.setVersion(serviceDTO.getVersion());
		entity.setCreateTime(serviceDTO.getCreateTime());
		entity.setLastUpdateTime(serviceDTO.getLastUpdateTime());

		return entity;
	}

	@GwtIncompatible
	public static List<Service> toEntities(List<ServiceDTO> serviceDTOs) {
		List<Service> entities = new ArrayList<>();
		if(serviceDTOs == null) return entities;

		for(ServiceDTO dto : serviceDTOs) {
			entities.add(toEntity(dto));
		}

		return entities;
	}

}

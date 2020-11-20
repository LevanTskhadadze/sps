package com.azry.sps.console.shared.dto.services;


import com.azry.sps.common.model.service.Service;
import com.azry.sps.console.shared.dto.ConfigurableDTO;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ServiceDto extends ConfigurableDTO implements IsSerializable {

	private long id;

	private String name;

	private String icon;

	private long groupId;

	private boolean allChannels;

	private List<ServiceChannelInfoDto> channels;

	private BigDecimal minAmount;

	private BigDecimal maxAmount;

	private String serviceDebtCode;

	private String servicePayCode;

	private String providerAccountIBAN;

	private boolean active;

	public ServiceDto(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public ServiceDto() {
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

	public List<ServiceChannelInfoDto> getChannels() {
		return channels;
	}

	public void setChannels(List<ServiceChannelInfoDto> channels) {
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
	public static ServiceDto toDto(Service service) {
		if (service != null) {
			ServiceDto dto = new ServiceDto();
 			dto.setId(service.getId());
			dto.setName(service.getName());
			dto.setIcon(service.getIcon());
			dto.setGroupId(service.getGroupId());
			dto.setAllChannels(service.isAllChannels());
			dto.setChannels(ServiceChannelInfoDto.toDtos(service.getChannels()));
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
	public static List<ServiceDto> toDtos(List<Service> services) {
		List<ServiceDto> dtos = new ArrayList<>();

		for(Service service : services) {
			dtos.add(toDto(service));
		}

		return dtos;
	}

	@GwtIncompatible
	public static Service toEntity(ServiceDto serviceDto) {
		Service entity = new Service();
		if(serviceDto == null) return entity;

		entity.setId(serviceDto.getId());
		entity.setName(serviceDto.getName());
		entity.setIcon(serviceDto.getIcon());
		entity.setGroupId(serviceDto.getGroupId());
		entity.setAllChannels(serviceDto.isAllChannels());
		entity.setChannels(ServiceChannelInfoDto.toEntities(serviceDto.getChannels()));
		entity.setProviderAccountIBAN(serviceDto.getProviderAccountIBAN());
		entity.setMinAmount(serviceDto.getMinAmount());
		entity.setMaxAmount(serviceDto.getMaxAmount());
		entity.setServiceDebtCode(serviceDto.getServiceDebtCode());
		entity.setServicePayCode(serviceDto.getServicePayCode());
		entity.setActive(serviceDto.isActive());
		entity.setVersion(serviceDto.getVersion());
		entity.setCreateTime(serviceDto.getCreateTime());
		entity.setLastUpdateTime(serviceDto.getLastUpdateTime());

		return entity;
	}

	@GwtIncompatible
	public static List<Service> toEntities(List<ServiceDto> serviceDtos) {
		List<Service> entities = new ArrayList<>();
		if(serviceDtos == null) return entities;

		for(ServiceDto dto : serviceDtos) {
			entities.add(toEntity(dto));
		}

		return entities;
	}

}

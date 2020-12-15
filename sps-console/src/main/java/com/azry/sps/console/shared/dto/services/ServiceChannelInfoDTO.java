package com.azry.sps.console.shared.dto.services;

import com.azry.sps.common.model.service.ServiceChannelInfo;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class ServiceChannelInfoDTO implements IsSerializable {

	private long channelId;

	private boolean active;

	private BigDecimal minAmount;

	private BigDecimal maxAmount;


	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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

	@GwtIncompatible
	public static ServiceChannelInfoDTO toDto(ServiceChannelInfo serviceChannelInfo) {
		ServiceChannelInfoDTO dto = new ServiceChannelInfoDTO();
		if (serviceChannelInfo == null) return dto;
		dto.setChannelId(serviceChannelInfo.getChannelId());
		dto.setMinAmount(serviceChannelInfo.getMinAmount());
		dto.setMaxAmount(serviceChannelInfo.getMaxAmount());
		dto.setActive(serviceChannelInfo.isActive());

		return dto;
	}

	@GwtIncompatible
	public static List<ServiceChannelInfoDTO> toDtos(List<ServiceChannelInfo> serviceChannelInfoList) {
		List<ServiceChannelInfoDTO> dtos = new ArrayList<>();
		if (serviceChannelInfoList == null) return dtos;
		for(ServiceChannelInfo service : serviceChannelInfoList) {
			dtos.add(toDto(service));
		}

		return dtos;
	}


	@GwtIncompatible
	public static ServiceChannelInfo toEntity(ServiceChannelInfoDTO dto) {
		ServiceChannelInfo entity = new ServiceChannelInfo();

		entity.setChannelId(dto.getChannelId());
		entity.setMinAmount(dto.getMinAmount());
		entity.setMaxAmount(dto.getMaxAmount());
		entity.setActive(dto.isActive());

		return entity;
	}

	@GwtIncompatible
	public static List<ServiceChannelInfo> toEntities(List<ServiceChannelInfoDTO> serviceChannelInfoDtoList) {
		List<ServiceChannelInfo> entities = new ArrayList<>();
		if (serviceChannelInfoDtoList == null) return entities;
		for(ServiceChannelInfoDTO dto : serviceChannelInfoDtoList) {
			entities.add(toEntity(dto));
		}

		return entities;
	}

	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof ServiceChannelInfoDTO)) return false;
		return ((ServiceChannelInfoDTO) obj).getChannelId() == getChannelId();
	}
}

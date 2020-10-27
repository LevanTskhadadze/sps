package com.azry.sps.console.shared.dto.services;

import com.azry.sps.common.model.service.ServiceChannelInfo;
import com.google.gwt.core.shared.GwtIncompatible;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class ServiceChannelInfoDto {

	private long channelId;

	private boolean active;

	private BigDecimal minAmount;

	private BigDecimal maxAmount;

	@GwtIncompatible
	public static ServiceChannelInfoDto toDto(ServiceChannelInfo serviceChannelInfo) {
		ServiceChannelInfoDto dto = new ServiceChannelInfoDto();
		if(serviceChannelInfo == null) return dto;
		dto.setChannelId(serviceChannelInfo.getChannelId());
		dto.setMinAmount(serviceChannelInfo.getMinAmount());
		dto.setMaxAmount(serviceChannelInfo.getMaxAmount());
		dto.setActive(serviceChannelInfo.isActive());

		return dto;
	}

	@GwtIncompatible
	public static List<ServiceChannelInfoDto> toDtos(List<ServiceChannelInfo> serviceChannelInfoList) {
		List<ServiceChannelInfoDto> dtos = new ArrayList<>();
		if(serviceChannelInfoList == null) return dtos;
		for(ServiceChannelInfo service : serviceChannelInfoList) {
			dtos.add(toDto(service));
		}

		return dtos;
	}


	@GwtIncompatible
	public static ServiceChannelInfo toEntity(ServiceChannelInfoDto dto) {
		ServiceChannelInfo entity = new ServiceChannelInfo();

		entity.setChannelId(dto.getChannelId());
		entity.setMinAmount(dto.getMinAmount());
		entity.setMaxAmount(dto.getMaxAmount());
		entity.setActive(dto.isActive());

		return entity;
	}

	@GwtIncompatible
	public static List<ServiceChannelInfo> toEntities(List<ServiceChannelInfoDto> serviceChannelInfoDtoList) {
		List<ServiceChannelInfo> entities = new ArrayList<>();
		if(serviceChannelInfoDtoList == null) return entities;
		for(ServiceChannelInfoDto dto : serviceChannelInfoDtoList) {
			entities.add(toEntity(dto));
		}

		return entities;
	}

	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof ServiceChannelInfoDto)) return false;
		return ((ServiceChannelInfoDto) obj).getChannelId() == getChannelId();
	}
}

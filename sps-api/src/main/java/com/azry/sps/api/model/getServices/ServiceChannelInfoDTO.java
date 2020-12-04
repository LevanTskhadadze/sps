package com.azry.sps.api.model.getServices;

import com.azry.sps.common.model.service.ServiceChannelInfo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@XmlType(name = "ServiceChannelInfo", propOrder = {"channelId", "active", "minAmount", "maxAmount"})
public class ServiceChannelInfoDTO {

	private long channelId;

	private boolean active;

	private BigDecimal minAmount;

	private BigDecimal maxAmount;

	@XmlElement(name = "channelId")
	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	@XmlElement(name = "active")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@XmlElement(name = "minAmount")
	public BigDecimal getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}

	@XmlElement(name = "maxAmount")
	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}

	public static ServiceChannelInfoDTO entityToInfo(com.azry.sps.common.model.service.ServiceChannelInfo entity) {
		ServiceChannelInfoDTO info = new ServiceChannelInfoDTO();

		info.setActive(entity.isActive());
		info.setChannelId(entity.getChannelId());
		info.setMinAmount(entity.getMinAmount());
		info.setMaxAmount(entity.getMaxAmount());

		return info;
	}

	public static List<ServiceChannelInfoDTO> entitiesToInfos(List<com.azry.sps.common.model.service.ServiceChannelInfo> entities) {
		List<ServiceChannelInfoDTO> infos = new ArrayList<>();

		for(ServiceChannelInfo entity : entities) {
			infos.add(entityToInfo(entity));
		}

		return infos;
	}
}

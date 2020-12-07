package com.azry.sps.api.model.getServices;


import com.azry.sps.common.model.service.Service;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@XmlType(name = "Service", propOrder = {"id", "name", "groupId", "allChannels", "channels", "minAmount", "maxAmount", "serviceDebtCode", "servicePayCode", "providerAccountIBAN", "active"})
public class ServiceDTO {

	private long id;

	private String name;

	private long groupId;

	private boolean allChannels;

	private List<ServiceChannelInfoDTO> channels;

	private BigDecimal minAmount;

	private BigDecimal maxAmount;

	private String serviceDebtCode;

	private String servicePayCode;

	private String providerAccountIBAN;

	private boolean active;

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

	@XmlElement(name = "groupId")
	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	@XmlElement(name = "allChannels")
	public boolean isAllChannels() {
		return allChannels;
	}

	public void setAllChannels(boolean allChannels) {
		this.allChannels = allChannels;
	}

	@XmlElement(name = "channels")
	public List<ServiceChannelInfoDTO> getChannels() {
		return channels;
	}

	public void setChannels(List<ServiceChannelInfoDTO> channels) {
		this.channels = channels;
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

	@XmlElement(name = "serviceDebtCode")
	public String getServiceDebtCode() {
		return serviceDebtCode;
	}

	public void setServiceDebtCode(String serviceDebtCode) {
		this.serviceDebtCode = serviceDebtCode;
	}

	@XmlElement(name = "servicePayCode")
	public String getServicePayCode() {
		return servicePayCode;
	}

	public void setServicePayCode(String servicePayCode) {
		this.servicePayCode = servicePayCode;
	}

	@XmlElement(name = "providerAccountIBAN")
	public String getProviderAccountIBAN() {
		return providerAccountIBAN;
	}

	public void setProviderAccountIBAN(String providerAccountIBAN) {
		this.providerAccountIBAN = providerAccountIBAN;
	}

	@XmlElement(name = "active")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public static ServiceDTO entityToInfo(Service service) {
		ServiceDTO info = new ServiceDTO();

		info.setActive(service.isActive());
		info.setAllChannels(service.isAllChannels());
		info.setChannels(ServiceChannelInfoDTO.entitiesToInfos(service.getChannels()));
		info.setGroupId(service.getGroupId());
		info.setId(service.getId());
		info.setMinAmount(service.getMinAmount());
		info.setMaxAmount(service.getMaxAmount());
		info.setName(service.getName());
		info.setProviderAccountIBAN(service.getProviderAccountIBAN());
		info.setServiceDebtCode(service.getServiceDebtCode());
		info.setServicePayCode(service.getServicePayCode());

		return info;
	}

	public static List<ServiceDTO> entitiesToInfos(List<Service> services) {
		List<ServiceDTO> infos = new ArrayList<>();

		for (Service service : services) {
			infos.add(entityToInfo(service));
		}
		return infos;
	}
}

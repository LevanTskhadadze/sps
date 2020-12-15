package com.azry.sps.api.model.getServices;


import com.azry.sps.common.model.service.Service;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@XmlType(name = "Service", propOrder = {"id", "name", "groupId", "minAmount", "maxAmount"})
public class ServiceDTO {

	private long id;

	private String name;

	private long groupId;

	private BigDecimal minAmount;

	private BigDecimal maxAmount;

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


	public static ServiceDTO entityToInfo(Service service) {
		ServiceDTO info = new ServiceDTO();

		info.setGroupId(service.getGroupId());
		info.setId(service.getId());
		info.setMinAmount(service.getMinAmount());
		info.setMaxAmount(service.getMaxAmount());
		info.setName(service.getName());


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

package com.azry.sps.api.model.getclientcommission;

import com.azry.sps.common.model.commission.CommissionRateType;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.math.BigDecimal;

@XmlType(name = "GetClientCommissionResponse", propOrder = {"id", "priority", "allServices", "allChannels", "servicesIds", "channelsIds", "rateType", "minCommission", "maxCommission", "commission"})
public class GetClientCommissionResponse implements Serializable {

	private long id;

	private long priority;

	private boolean allServices;

	private String servicesIds;

	private boolean allChannels;

	private String channelsIds;


	private CommissionRateType rateType;

	private BigDecimal minCommission;

	private BigDecimal maxCommission;

	private BigDecimal Commission;

	@XmlElement(name = "id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@XmlElement(name = "priority")
	public long getPriority() {
		return priority;
	}

	public void setPriority(long priority) {
		this.priority = priority;
	}

	@XmlElement(name = "allServices")
	public boolean isAllServices() {
		return allServices;
	}

	public void setAllServices(boolean allServices) {
		this.allServices = allServices;
	}

	@XmlElement(name = "serviceIds")
	public String getServicesIds() {
		return servicesIds;
	}

	public void setServicesIds(String servicesIds) {
		this.servicesIds = servicesIds;
	}

	@XmlElement(name = "allChannels")
	public boolean isAllChannels() {
		return allChannels;
	}

	public void setAllChannels(boolean allChannels) {
		this.allChannels = allChannels;
	}

	@XmlElement(name = "channelIds")
	public String getChannelsIds() {
		return channelsIds;
	}

	public void setChannelsIds(String channelsIds) {
		this.channelsIds = channelsIds;
	}

	@XmlElement(name = "rateType")
	public CommissionRateType getRateType() {
		return rateType;
	}

	public void setRateType(CommissionRateType rateType) {
		this.rateType = rateType;
	}

	@XmlElement(name = "minCommission")
	public BigDecimal getMinCommission() {
		return minCommission;
	}

	public void setMinCommission(BigDecimal minCommission) {
		this.minCommission = minCommission;
	}

	@XmlElement(name = "maxCommission")
	public BigDecimal getMaxCommission() {
		return maxCommission;
	}

	public void setMaxCommission(BigDecimal maxCommission) {
		this.maxCommission = maxCommission;
	}

	@XmlElement(name = "commission")
	public BigDecimal getCommission() {
		return Commission;
	}

	public void setCommission(BigDecimal commission) {
		Commission = commission;
	}
}

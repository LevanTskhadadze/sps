package com.azry.sps.server.services.payment;

import java.util.Date;

public class PaymentParams {
	Long id;

	String agentPaymentId;

	Date creationStartTime;

	Date creationEndTime;

	Long serviceId;

	Long channelId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAgentPaymentId() {
		return agentPaymentId;
	}

	public void setAgentPaymentId(String agentPaymentId) {
		this.agentPaymentId = agentPaymentId;
	}

	public Date getCreationStartTime() {
		return creationStartTime;
	}

	public void setCreationStartTime(Date creationStartTime) {
		this.creationStartTime = creationStartTime;
	}

	public Date getCreationEndTime() {
		return creationEndTime;
	}

	public void setCreationEndTime(Date creationEndTime) {
		this.creationEndTime = creationEndTime;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

}

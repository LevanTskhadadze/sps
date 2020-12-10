package com.azry.sps.api.model.calculateclientcommission;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.math.BigDecimal;

@XmlType(name = "CalculateClientCommissionRequest", propOrder = {"serviceId", "channelId", "amount"})
public class CalculateClientCommissionRequest implements Serializable {

	Long serviceId;

	Long channelId;

	BigDecimal amount;

	@XmlElement(name = "serviceId", required = true)
	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	@XmlElement(name = "channelId", required = true)
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public boolean isValid() {
		return serviceId != null && channelId != null;
	}

	@XmlElement(name = "amount", required = true)
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}

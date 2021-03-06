package com.azry.sps.common.model.service;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
public class ServiceChannelInfo {

	private long channelId;

	private boolean active;

	@Column(precision = 10, scale = 2)
	private BigDecimal minAmount;

	@Column(precision = 10, scale = 2)
	private BigDecimal maxAmount;

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ServiceChannelInfo)) return false;

		return channelId  == ((ServiceChannelInfo) obj).getChannelId();
	}

}

package com.azry.sps.common.model.service;


import com.azry.sps.common.model.Configurable;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Service extends Configurable {

	private long id;

	private String name;

	private String icon;

	private long groupId;

	private boolean allChannels;

	private List<ServiceChannelInfo> channels;

	private BigDecimal minAmount;

	private BigDecimal maxAmount;

	private String serviceDebtCode;

	private String servicePayCode;

	private String providerAccountIBAN;

	private boolean active;
}

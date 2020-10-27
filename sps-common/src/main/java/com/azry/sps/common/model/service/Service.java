package com.azry.sps.common.model.service;


import com.azry.sps.common.model.Configurable;
import com.azry.sps.common.utils.XmlUtils;
import lombok.Data;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.List;

@Data
@XmlRootElement
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

	@Transient
	public ServiceEntity getEntity(){
		ServiceEntity entity = new ServiceEntity();
		entity.setData(XmlUtils.toXml(this));
		entity.setId(getId());
		entity.setName(getName());
		entity.setActive(isActive());
		entity.setLastUpdateTime(getLastUpdateTime());
		entity.setCreateTime(getCreateTime());
		entity.setVersion(getVersion());
		return entity;
	}

}

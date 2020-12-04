package com.azry.sps.api.model.getclientcommission;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "GetClientCommissionRequest", propOrder = {"serviceId", "channelId"})
public class GetClientCommissionRequest implements Serializable {

	long serviceId;

	long channelId;

	@XmlElement(name = "serviceId")
	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	@XmlElement(name = "channelId")
	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}
}

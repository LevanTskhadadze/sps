package com.azry.sps.api.model.getclientcommission;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "GetClientCommissionRequest", propOrder = {"serviceId", "channelId"})
public class GetClientCommissionRequest implements Serializable {

	Long serviceId;

	Long channelId;

	@XmlElement(name = "serviceId")
	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	@XmlElement(name = "channelId")
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public boolean isValid() {
		return serviceId != null && channelId != null;
	}

}

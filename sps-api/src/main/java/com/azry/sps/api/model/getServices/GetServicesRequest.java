package com.azry.sps.api.model.getServices;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(name = "GetServicesRequest", propOrder = {"channelId"})
public class GetServicesRequest implements Serializable {

	Long channelId;

	@XmlElement(name = "channelId", required = true)
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public boolean isValid() {
		return channelId != null;
	}
}

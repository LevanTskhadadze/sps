package com.azry.sps.console.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;


public class ConfigurableDTO implements IsSerializable {

	private Date createTime;

	private Date lastUpdateTime;

	private long version;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
}

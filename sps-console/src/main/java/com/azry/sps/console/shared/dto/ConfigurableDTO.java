package com.azry.sps.console.shared.dto;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;


public class ConfigurableDTO implements IsSerializable {

	private Date createTime;

	private Date lastUpdateTime;

	private long version;

	public Date getCreateTime() {
		return createTime;
	}

	public String getFormattedCreateTime() {

		if (createTime == null) {
			return "";
		}
		DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy/MM/dd hh:mm:ss");
		return dateFormat.format(createTime);
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public String getFormattedLastUpdateTime() {

		if (lastUpdateTime == null) {
			return "";
		}
		DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy/MM/dd hh:mm:ss");
		return dateFormat.format(lastUpdateTime);
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

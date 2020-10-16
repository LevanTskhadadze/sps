package com.azry.sps.console.shared.dto.systemparameter;


import com.google.gwt.user.client.rpc.IsSerializable;


public class SystemParameterDto implements IsSerializable {
	private long id;

	private String code;

	private String value;

	private SystemParameterDtoType type;

	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public SystemParameterDtoType getType() {
		return type;
	}

	public void setType(SystemParameterDtoType type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SystemParameterDto)) {
			return false;
		}
		return id == ((SystemParameterDto)obj).id;
	}
}

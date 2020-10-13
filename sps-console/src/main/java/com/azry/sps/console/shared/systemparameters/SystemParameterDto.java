package com.azry.sps.console.shared.systemparameters;


import java.io.Serializable;

public class SystemParameterDto implements Serializable {
	private long id;

	private String code;

	private String value;

	private SystemParameterDtoType type;

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
}

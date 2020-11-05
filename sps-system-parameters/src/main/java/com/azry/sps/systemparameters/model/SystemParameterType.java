package com.azry.sps.systemparameters.model;


public enum SystemParameterType {

	STRING("string"),
	INTEGER("integer"),
	BOOLEAN("boolean");

	String name;

	SystemParameterType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

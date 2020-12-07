package com.azry.sps.systemparameters.sysparam;

import java.io.Serializable;

public class Parameter<T> implements Serializable {

	private String key;

	private T value;

	public Parameter(T value) {
		this.value = value;
	}


	public Parameter(String key, T value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "key=" + key + ", value=" + (value == null ? "" : value.toString());
	}
}
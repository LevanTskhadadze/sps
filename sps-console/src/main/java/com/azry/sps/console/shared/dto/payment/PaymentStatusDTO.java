package com.azry.sps.console.shared.dto.payment;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum PaymentStatusDTO implements IsSerializable {
	CREATED("orange"),
	COLLECT_PENDING("#ffeb60"),
	COLLECTED("#6cb96c"),
	COLLECT_REJECTED("orangered"),
	PENDING("orange"),
	PERFORMED("green"),
	REJECTED("red");

	String color;

	PaymentStatusDTO(String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}
}
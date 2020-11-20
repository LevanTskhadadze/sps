package com.azry.sps.console.shared.dto.payment;

public enum PaymentStatusDto {
	CREATED("blue"),
	COLLECT_PENDING("yellow"),
	COLLECTED("lightgreen"),
	COLLECT_REJECTED("orangered"),
	PENDING("orange"),
	PERFORMED("green"),
	REJECTED("red");

	String color;

	PaymentStatusDto(String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}
}
package com.azry.sps.common.model.client;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Date;

@Embeddable
@Data
public class Client {

	@Column(length = 20)
	private String personalNumber;

	@Column(length = 100)
	private String firstName;

	@Column(length = 100)
	private String lastName;

	private Date birthDate;

	@Column(length = 100)
	private String birthPlace;
/*
	//TODO შესამოწმებელია
	@Override
	public boolean equals(Object object) {
		if (object == this) return true;
		if (!(object instanceof Client)) {
			return false;
		}

		return personalNumber.equals(((Client) object).getPersonalNumber());
	}*/
}
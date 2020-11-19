package com.azry.sps.console.shared.dto.bankclient;

import com.azry.sps.fi.bankws.Client;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class ClientDTO implements IsSerializable {

	private long id;

	private String personalNumber;

	private String firstName;

	private String lastName;

	private Date birthDate;

	private String birthPlace;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPersonalNumber() {
		return personalNumber;
	}

	public void setPersonalNumber(String personalNumber) {
		this.personalNumber = personalNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}


	@GwtIncompatible
	public static ClientDTO toDTO(Client client) {
		if (client != null) {
			ClientDTO dto = new ClientDTO();
			dto.setId(client.getId());
			dto.setPersonalNumber(client.getPersonalNumber());
			dto.setFirstName(client.getFirstName());
			dto.setLastName(client.getLastName());
			dto.setBirthDate(client.getBirthDate());
			dto.setBirthPlace(client.getBirthPlace());

			return dto;
		}
		return null;
	}

	@GwtIncompatible
	public static ClientDTO toDTO(com.azry.sps.common.model.client.Client client) {
		if (client != null) {
			ClientDTO dto = new ClientDTO();
			dto.setPersonalNumber(client.getPersonalNumber());
			dto.setFirstName(client.getFirstName());
			dto.setLastName(client.getLastName());
			dto.setBirthDate(client.getBirthDate());
			dto.setBirthPlace(client.getBirthPlace());

			return dto;
		}
		return null;
	}


	@GwtIncompatible
	public Client bankClientFromDTO() {
		Client client = new Client();
		client.setId(this.getId());
		client.setPersonalNumber(this.getPersonalNumber());
		client.setFirstName(this.getFirstName());
		client.setLastName(this.getLastName());
		client.setBirthDate(this.getBirthDate());
		client.setBirthPlace(this.getBirthPlace());

		return client;
	}

	@GwtIncompatible
	public com.azry.sps.common.model.client.Client entityFromDTO() {
		com.azry.sps.common.model.client.Client client = new com.azry.sps.common.model.client.Client();
		client.setPersonalNumber(this.getPersonalNumber());
		client.setFirstName(this.getFirstName());
		client.setLastName(this.getLastName());
		client.setBirthDate(this.getBirthDate());
		client.setBirthPlace(this.getBirthPlace());

		return client;
	}
}

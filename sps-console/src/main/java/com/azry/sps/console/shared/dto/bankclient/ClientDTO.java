package com.azry.sps.console.shared.dto.bankclient;

import com.azry.sps.common.model.client.Client;
import com.azry.sps.console.shared.dto.paymentList.PaymentListDTO;
import com.azry.sps.fi.model.BankClient;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;
import java.util.List;

public class ClientDTO implements IsSerializable {

	private long id;

	private String personalNumber;

	private String firstName;

	private String lastName;

	private Date birthDate;

	private String birthPlace;

	private List<AccountDTO> accountDTOs;

	private PaymentListDTO paymentListDTO;

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

	public List<AccountDTO> getAccountDTOs() {
		return accountDTOs;
	}

	public void setAccountDTOs(List<AccountDTO> accountDTOs) {
		this.accountDTOs = accountDTOs;
	}

	public PaymentListDTO getPaymentListDTO() {
		return paymentListDTO;
	}

	public void setPaymentListDTO(PaymentListDTO paymentListDTO) {
		this.paymentListDTO = paymentListDTO;
	}

	@GwtIncompatible
	public static ClientDTO bankClientToDTO(BankClient client) {
		if (client != null) {
			ClientDTO dto = new ClientDTO();
			dto.setId(client.getId());
			dto.setPersonalNumber(client.getPersonalNumber());
			dto.setFirstName(client.getFirstName());
			dto.setLastName(client.getLastName());
			dto.setBirthDate(client.getBirthDate());
			dto.setBirthPlace(client.getBirthPlace());
			dto.setAccountDTOs(AccountDTO.toDTOs(client.getAccounts()));

			return dto;
		}
		return null;
	}

	@GwtIncompatible
	public static ClientDTO EntityToDTO(Client client) {
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
	public BankClient bankClientFromDTO() {
		BankClient client = new BankClient();
		client.setId(this.getId());
		client.setPersonalNumber(this.getPersonalNumber());
		client.setFirstName(this.getFirstName());
		client.setLastName(this.getLastName());
		client.setBirthDate(this.getBirthDate());
		client.setBirthPlace(this.getBirthPlace());
		client.setAccounts(AccountDTO.fromDTOs(this.accountDTOs));

		return client;
	}

	@GwtIncompatible
	public Client entityFromDTO() {
		com.azry.sps.common.model.client.Client client = new com.azry.sps.common.model.client.Client();
		client.setPersonalNumber(this.getPersonalNumber());
		client.setFirstName(this.getFirstName());
		client.setLastName(this.getLastName());
		client.setBirthDate(this.getBirthDate());
		client.setBirthPlace(this.getBirthPlace());

		return client;
	}
}

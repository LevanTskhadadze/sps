package com.azry.sps.fi.model;

import com.azry.sps.fi.bankws.Client;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class BankClient {

	private long id;

	private String personalNumber;

	private String firstName;

	private String lastName;

	private Date birthDate;

	private String birthPlace;

	private List<Account> accounts = new ArrayList<>();

	public BankClient(Client client) {
		this.id = client.getId();
		this.personalNumber = client.getPersonalNumber();
		this.firstName = client.getFirstName();
		this.lastName = client.getLastName();
		this.birthDate = client.getBirthDate();
		this.birthPlace = client.getBirthPlace();
	}
	
	public void mapClientAccounts(List<com.azry.sps.fi.bankws.Account> accounts) {
		for (com.azry.sps.fi.bankws.Account account : accounts) {
			this.accounts.add(new Account(account));
		}
	}
}

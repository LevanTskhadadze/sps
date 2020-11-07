package com.azry.sps.console.shared.dto.bankclient;

import com.azry.sps.fi.bankws.Account;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountDTO implements IsSerializable {

	private String iban;

	private String currency;

	private BigDecimal balance;

	private String name;


	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@GwtIncompatible
	public static AccountDTO toDTO(Account account) {
		if (account != null) {
			AccountDTO dto = new AccountDTO();
			dto.setIban(account.getIban());
			dto.setCurrency(account.getCurrency());
			dto.setBalance(account.getBalance());
			dto.setName(account.getName());

			return dto;
		}
		return null;
	}

	@GwtIncompatible
	public static List<AccountDTO> toDTOs(List<Account> accounts) {
		if (accounts != null) {
			List<AccountDTO> dtos = new ArrayList<>();

			for (Account account : accounts) {
				dtos.add(toDTO(account));

			}
			return dtos;
		}
		return null;
	}


	@GwtIncompatible
	public Account fromDTO() {
		Account account = new Account();
		account.setIban(this.getIban());
		account.setCurrency(this.getCurrency());
		account.setBalance(this.getBalance());
		account.setName(this.getName());

		return account;
	}
}

package com.azry.sps.fi.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class Account {

	private String iban;

	private String currency;

	private BigDecimal balance;

	private String name;

	public Account(com.azry.sps.fi.bankws.Account account) {
		this.iban = account.getIban();
		this.currency = account.getCurrency();
		this.balance = account.getBalance();
		this.name = account.getName();
	}
}

package com.azry.sps.fi.model.transaction;

import com.azry.sps.fi.bankws.TransactionResponseEntry;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FiTransactionResponseEntry {

	private String transactionId;

	private String fiId;

	public FiTransactionResponseEntry(TransactionResponseEntry entry) {
		this.transactionId = entry.getTransactionId();
		this.fiId = entry.getFiId();
	}
}
package com.azry.sps.fi.model.transaction;

import com.azry.sps.fi.bankws.TransactionResponse;
import com.azry.sps.fi.bankws.TransactionResponseEntry;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FiTransactionResponse {

	private List<FiTransactionResponseEntry> transactionResponseEntries = new ArrayList<>();

	public FiTransactionResponse(TransactionResponse transactionResponse) {
		for (TransactionResponseEntry entry : transactionResponse.getTransactionResponseEntry()) {
			this.transactionResponseEntries.add(new FiTransactionResponseEntry(entry));
		}
	}
}
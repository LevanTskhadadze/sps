package com.azry.sps.fi.model.transaction;

import com.azry.sps.fi.bankws.TransactionRequest;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FiTransactionRequest {

	private List<FiTransaction> transactions = new ArrayList<>();

	public TransactionRequest toTransactionRequest() {
		TransactionRequest transactionRequest = new TransactionRequest();
		for (FiTransaction transaction : transactions) {
			transactionRequest.getTransaction().add(transaction.toTransaction());
		}
		return transactionRequest;
	}
}
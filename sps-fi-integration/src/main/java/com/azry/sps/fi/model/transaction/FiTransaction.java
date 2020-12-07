package com.azry.sps.fi.model.transaction;

import com.azry.sps.fi.bankws.Transaction;
import com.azry.sps.fi.bankws.TransactionType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class FiTransaction {

	private String transactionId;

	private String sourceAccount;

	private String destinationAccount;

	private BigDecimal amount;

	private String purpose;

	private FiTransactionType type;

	private String clientPersonalNumber;

	public Transaction toTransaction() {
		Transaction transaction = new Transaction();
		transaction.setTransactionId(transactionId);
		transaction.setSourceAccount(sourceAccount);
		transaction.setDestinationAccount(destinationAccount);
		transaction.setAmount(amount);
		transaction.setPurpose(purpose);
		transaction.setType(TransactionType.valueOf(type.name()));
		transaction.setClientPersonalNumber(clientPersonalNumber);

		return transaction;
	}
}
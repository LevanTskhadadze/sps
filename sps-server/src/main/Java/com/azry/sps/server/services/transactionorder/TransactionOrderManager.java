package com.azry.sps.server.services.transactionorder;

import com.azry.sps.common.model.payment.Payment;
import com.azry.sps.common.model.transaction.TransactionOrder;
import com.azry.sps.common.model.transaction.TransactionType;

import javax.ejb.Local;
import java.util.List;

@Local
public interface TransactionOrderManager {

	TransactionOrder getTransaction(long paymentId, TransactionType type);

	void changeTransactions(List<TransactionOrder> transactions);

	void addTransaction(TransactionOrder transaction);

	void addTransaction(String sourceAccountIBAN, String clientAccountIBAN, Payment payment);

	void addTransactions(String sourceAccountBan, String clientAccountIban, List<Payment> payments);
}

package com.azry.sps.server.services.transactionorder;

import com.azry.sps.common.model.transaction.TransactionOrder;
import com.azry.sps.common.model.transaction.TransactionType;

import javax.ejb.Local;

@Local
public interface TransactionOrderManager {

	TransactionOrder getTransaction(long paymentId, TransactionType type);

}

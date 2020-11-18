package com.azry.sps.console.shared.transactionorder;

import com.azry.sps.common.model.transaction.TransactionOrder;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDto;
import com.azry.sps.console.shared.dto.transactionorder.TransactionTypeDto;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;
import java.util.Map;

public interface TransactionOrderServiceAsync {
	void getTransaction(long paymentId, TransactionTypeDto type, AsyncCallback<TransactionOrder> callback);
}

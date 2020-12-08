package com.azry.sps.console.shared.transactionorder;

import com.azry.sps.console.shared.dto.transactionorder.TransactionOrderDTO;
import com.azry.sps.console.shared.dto.transactionorder.TransactionTypeDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("servlet/TransactionOrderService")
public interface TransactionOrderService extends RemoteService  {

	TransactionOrderDTO getTransaction(long paymentId, TransactionTypeDTO type);
}

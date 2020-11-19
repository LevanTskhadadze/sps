package com.azry.sps.console.shared.transactionorder;

import com.azry.sps.console.shared.dto.transactionorder.TransactionOrderDto;
import com.azry.sps.console.shared.dto.transactionorder.TransactionTypeDto;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("servlet/sysPar")
public interface TransactionOrderService extends RemoteService  {

		TransactionOrderDto getTransaction(long paymentId, TransactionTypeDto type);

}

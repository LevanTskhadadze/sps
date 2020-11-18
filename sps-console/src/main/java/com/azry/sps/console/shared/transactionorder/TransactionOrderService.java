package com.azry.sps.console.shared.transactionorder;

import com.azry.sps.common.model.transaction.TransactionOrder;
import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDto;
import com.azry.sps.console.shared.dto.transactionorder.TransactionOrderDto;
import com.azry.sps.console.shared.dto.transactionorder.TransactionTypeDto;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;
import java.util.Map;

@RemoteServiceRelativePath("servlet/sysPar")
public interface TransactionOrderService extends RemoteService  {

		TransactionOrderDto getTransaction(long paymentId, TransactionTypeDto type);

}

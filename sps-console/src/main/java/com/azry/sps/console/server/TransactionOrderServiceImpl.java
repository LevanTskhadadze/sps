package com.azry.sps.console.server;

import com.azry.sps.console.shared.dto.transactionorder.TransactionOrderDTO;
import com.azry.sps.console.shared.dto.transactionorder.TransactionTypeDTO;
import com.azry.sps.console.shared.transactionorder.TransactionOrderService;
import com.azry.sps.server.services.transactionorder.TransactionOrderManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;

@WebServlet("sps/servlet/TransactionOrderService")
public class TransactionOrderServiceImpl extends RemoteServiceServlet implements TransactionOrderService {

	@Inject
	TransactionOrderManager transactionOrderManager;

	@Override
	public TransactionOrderDTO getTransaction(long paymentId, TransactionTypeDTO type) {
		return TransactionOrderDTO.toDTO(transactionOrderManager.getTransaction(paymentId, TransactionOrderDTO.dtoToType(type)));
	}
}

package com.azry.sps.console.server;

import com.azry.sps.console.shared.dto.transactionorder.TransactionOrderDTO;
import com.azry.sps.console.shared.dto.transactionorder.TransactionTypeDTO;
import com.azry.sps.console.shared.transactionorder.TransactionOrderService;
import com.azry.sps.server.services.transactionorder.TransactionOrderManager;

import javax.inject.Inject;

public class TransactionOrderServiceImpl implements TransactionOrderService {

	@Inject
	TransactionOrderManager transactionOrderManager;

	@Override
	public TransactionOrderDTO getTransaction(long paymentId, TransactionTypeDTO type) {
		return TransactionOrderDTO.toDTO(transactionOrderManager.getTransaction(paymentId, TransactionOrderDTO.dtoToType(type)));
	}
}

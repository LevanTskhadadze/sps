package com.azry.sps.console.server;

import com.azry.sps.console.shared.dto.transactionorder.TransactionOrderDto;
import com.azry.sps.console.shared.dto.transactionorder.TransactionTypeDto;
import com.azry.sps.console.shared.transactionorder.TransactionOrderService;
import com.azry.sps.server.services.transactionorder.TransactionOrderManager;

import javax.inject.Inject;

public class TransactionOrderServiceImpl implements TransactionOrderService {

	@Inject
	TransactionOrderManager transactionOrderManager;

	@Override
	public TransactionOrderDto getTransaction(long paymentId, TransactionTypeDto type) {
		return TransactionOrderDto.toDto(transactionOrderManager.getTransaction(paymentId, TransactionOrderDto.dtoToType(type)));
	}
}

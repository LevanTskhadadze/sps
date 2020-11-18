package com.azry.sps.server.services.transactionorder;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.exceptions.SPSException;
import com.azry.sps.common.model.service.Service;
import com.azry.sps.common.model.service.ServiceEntity;
import com.azry.sps.common.model.transaction.TransactionOrder;
import com.azry.sps.common.model.transaction.TransactionType;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;

@Local
public interface TransactionOrderManager {

	TransactionOrder getTransaction(long paymentId, TransactionType type);

}

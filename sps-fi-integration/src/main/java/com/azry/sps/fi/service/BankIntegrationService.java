package com.azry.sps.fi.service;


import com.azry.sps.fi.model.BankClient;
import com.azry.sps.fi.model.exception.FIConnectivityException;
import com.azry.sps.fi.model.exception.FIException;
import com.azry.sps.fi.model.transaction.FiTransactionRequest;
import com.azry.sps.fi.model.transaction.FiTransactionResponse;

import javax.ejb.Local;

@Local
public interface BankIntegrationService {

	BankClient getClientWithAccount(String personalNumber) throws FIException, FIConnectivityException;

	FiTransactionResponse processTransactions(FiTransactionRequest request) throws FIException, FIConnectivityException;
}

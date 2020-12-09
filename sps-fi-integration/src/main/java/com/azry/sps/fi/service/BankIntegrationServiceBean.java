package com.azry.sps.fi.service;


import com.azry.sps.fi.bankws.BankService;
import com.azry.sps.fi.bankws.BankServiceException_Exception;
import com.azry.sps.fi.bankws.BankServiceImplService;
import com.azry.sps.fi.inteceptor.FIExceptionInterceptor;
import com.azry.sps.fi.model.BankClient;
import com.azry.sps.fi.model.exception.FIConnectivityException;
import com.azry.sps.fi.model.exception.FIException;
import com.azry.sps.fi.model.transaction.FiTransactionRequest;
import com.azry.sps.fi.model.transaction.FiTransactionResponse;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

@Stateless(name = "BankIntegrationServiceBean")
@Interceptors(FIExceptionInterceptor.class)
public class BankIntegrationServiceBean implements BankIntegrationService {

	BankServiceImplService service = new BankServiceImplService();
	BankService bankService = service.getBankServiceImplPort();

	public BankClient getClientWithAccount(String personalNumber) throws FIException, FIConnectivityException {
		try  {
			BankClient client = new BankClient(bankService.findClient(personalNumber));
			client.mapClientAccounts(bankService.getClientAccounts(client.getId()));
			return client;
		} catch (BankServiceException_Exception ex) {
			throw new FIException(ex);
		}
	}

	@Override
	public FiTransactionResponse processTransactions(FiTransactionRequest request) throws FIException, FIConnectivityException {
		try {
			return new FiTransactionResponse(bankService.processTransactions(request.toTransactionRequest()));
		} catch (BankServiceException_Exception ex) {
			throw new FIException(ex);
		}
	}
}

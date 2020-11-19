package com.azry.sps.fi.service;


import com.azry.sps.common.exceptions.SPSException;
import com.azry.sps.fi.bankws.Account;
import com.azry.sps.fi.bankws.BankService;
import com.azry.sps.fi.bankws.BankServiceException_Exception;
import com.azry.sps.fi.bankws.BankServiceImplService;
import com.azry.sps.fi.bankws.Client;
import com.azry.sps.fi.bankws.TransactionRequest;
import com.azry.sps.fi.bankws.TransactionResponse;
import com.azry.sps.fi.inteceptor.FIExceptionInterceptor;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import java.util.List;

@Stateless
@Interceptors(FIExceptionInterceptor.class)
public class BankIntegrationManagerBean implements BankIntegrationManager {

	BankServiceImplService service = new BankServiceImplService();
	BankService bankService = service.getBankServiceImplPort();


	@Override
	public Client findClient(String personalNumber) throws SPSException {
		try {
			return bankService.findClient(personalNumber);
		} catch (BankServiceException_Exception ex) {
			throw new SPSException(ex.getFaultInfo().getCode());
		}

	}

	@Override
	public List<Account> getClientAccounts(long clientId) {
		return bankService.getClientAccounts(clientId);
	}

	@Override
	public TransactionResponse processTransactions(TransactionRequest request) throws BankServiceException_Exception {
		return bankService.processTransactions(request);
	}
}

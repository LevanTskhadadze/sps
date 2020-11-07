package com.azry.sps.fi.service;


import com.azry.sps.common.exceptions.SPSException;
import com.azry.sps.fi.bankws.Account;
import com.azry.sps.fi.bankws.BankServiceException_Exception;
import com.azry.sps.fi.bankws.Client;
import com.azry.sps.fi.bankws.TransactionRequest;
import com.azry.sps.fi.bankws.TransactionResponse;

import javax.ejb.Local;
import java.util.List;

@Local
public interface BankIntegrationManager {

	Client findClient(String personalNumber) throws SPSException;

	List<Account> getClientAccounts(long clientId);

	TransactionResponse processTransactions(TransactionRequest request) throws BankServiceException_Exception;
}

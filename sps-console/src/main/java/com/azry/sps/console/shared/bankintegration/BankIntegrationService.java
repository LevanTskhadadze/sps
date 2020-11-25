package com.azry.sps.console.shared.bankintegration;

import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.bankclient.ClientDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("servlet/BankIntegration")
public interface BankIntegrationService extends RemoteService {

	ClientDTO getClientWithAccount(String personalNumber) throws SPSConsoleException;

	ClientDTO getBankClientWithPaymentList(String personalNumber) throws SPSConsoleException;

//	ClientDTO findClient(String personalNumber) throws SPSConsoleException;
//
//	List<AccountDTO> getClientAccounts(long clientId);
}

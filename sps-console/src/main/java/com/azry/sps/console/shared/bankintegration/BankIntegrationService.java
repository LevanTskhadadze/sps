package com.azry.sps.console.shared.bankintegration;

import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.bankclient.AccountDTO;
import com.azry.sps.console.shared.dto.bankclient.ClientDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("servlet/BankIntegration")
public interface BankIntegrationService extends RemoteService {

	ClientDTO findClient(String personalNumber) throws SPSConsoleException;

	List<AccountDTO> getClientAccounts(long clientId);
}

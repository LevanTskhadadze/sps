package com.azry.sps.console.shared.bankintegration;

import com.azry.sps.console.shared.dto.bankclient.AccountDTO;
import com.azry.sps.console.shared.dto.bankclient.ClientDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface BankIntegrationServiceAsync {

	void findClient(String personalNumber, AsyncCallback<ClientDTO> async);

	void getClientAccounts(long clientId, AsyncCallback<List<AccountDTO>> async);
}

package com.azry.sps.console.shared.bankintegration;

import com.azry.sps.console.shared.dto.bankclient.ClientDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BankIntegrationServiceAsync {

	void getBankClientWithPaymentList(String personalNumber, AsyncCallback<ClientDTO> async);

	void getClientWithAccount(String personalNumber, AsyncCallback<ClientDTO> async);

//	void findClient(String personalNumber, AsyncCallback<ClientDTO> async);
//
//	void getClientAccounts(long clientId, AsyncCallback<List<AccountDTO>> async);
}

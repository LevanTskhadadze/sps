package com.azry.sps.console.shared.paymentlist;

import com.azry.sps.console.shared.dto.bankclient.ClientDTO;
import com.azry.sps.console.shared.dto.paymentList.PaymentListDTO;
import com.azry.sps.console.shared.dto.paymentList.PaymentListEntryDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PaymentListServiceAsync {

	void getPaymentList(String personalNumber, AsyncCallback<PaymentListDTO> async);

	void getPaymentListWithServices(String personalNumber, AsyncCallback<PaymentListDTO> async);

	void addPaymentListEntry(ClientDTO client, PaymentListEntryDTO paymentListEntry, AsyncCallback<PaymentListEntryDTO> async);

	void deletePaymentListEntry(long id, AsyncCallback<Void> async);
}

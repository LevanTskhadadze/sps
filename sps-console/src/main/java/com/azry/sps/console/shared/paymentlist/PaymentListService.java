package com.azry.sps.console.shared.paymentlist;

import com.azry.sps.console.shared.dto.bankclient.ClientDTO;
import com.azry.sps.console.shared.dto.paymentList.PaymentListDTO;
import com.azry.sps.console.shared.dto.paymentList.PaymentListEntryDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("servlet/PaymentList")
public interface PaymentListService extends RemoteService {

	PaymentListDTO getPaymentList(String personalNumber);

	PaymentListDTO addPaymentListEntry(ClientDTO client, PaymentListEntryDTO paymentListEntry);
}

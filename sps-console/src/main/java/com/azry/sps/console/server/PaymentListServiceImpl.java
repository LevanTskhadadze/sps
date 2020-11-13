package com.azry.sps.console.server;

import com.azry.sps.common.model.paymentlist.PaymentList;
import com.azry.sps.console.shared.dto.bankclient.ClientDTO;
import com.azry.sps.console.shared.dto.paymentList.PaymentListDTO;
import com.azry.sps.console.shared.dto.paymentList.PaymentListEntryDTO;
import com.azry.sps.console.shared.paymentlist.PaymentListService;
import com.azry.sps.server.services.paymentlist.PaymentListManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;

@WebServlet("sps/servlet/PaymentList")
public class PaymentListServiceImpl extends RemoteServiceServlet implements PaymentListService {

	@Inject
	PaymentListManager paymentListManager;

	@Override
	public PaymentListDTO getPaymentList(String personalNumber) {
		return PaymentListDTO.toDTO(paymentListManager.getPaymentList(personalNumber));
	}

	@Override
	public PaymentListDTO addPaymentListEntry(ClientDTO client, PaymentListEntryDTO paymentListEntry) {
		PaymentList paymentList = paymentListManager.addPaymentListEntry(client.entityFromDTO(), paymentListEntry.fromDTO());
		return PaymentListDTO.toDTO(paymentList);
	}
}

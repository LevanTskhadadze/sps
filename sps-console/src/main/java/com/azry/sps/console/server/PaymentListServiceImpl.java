package com.azry.sps.console.server;

import com.azry.sps.common.exceptions.SPSException;
import com.azry.sps.common.model.paymentlist.PaymentListEntry;
import com.azry.sps.console.shared.clientexception.SPSConsoleException;
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
	public PaymentListEntryDTO addPaymentListEntry(ClientDTO client, PaymentListEntryDTO paymentListEntry) throws SPSConsoleException {
		try {
			PaymentListEntry paymentList = paymentListManager.addPaymentListEntry(client.entityFromDTO(), paymentListEntry.fromDTO());
			return PaymentListEntryDTO.toDTO(paymentList);
		}
		catch (SPSException ex) {
			throw new SPSConsoleException(ex);
		}
	}

	@Override
	public void deletePaymentListEntry(long id) {
		paymentListManager.deletePaymentListEntry(id);
	}
}

package com.azry.sps.console.server;

import com.azry.sps.common.model.paymentlist.PaymentListEntry;
import com.azry.sps.common.model.service.Service;
import com.azry.sps.console.shared.dto.bankclient.ClientDTO;
import com.azry.sps.console.shared.dto.paymentList.PaymentListDTO;
import com.azry.sps.console.shared.dto.paymentList.PaymentListEntryDTO;
import com.azry.sps.console.shared.dto.services.ServiceDTO;
import com.azry.sps.console.shared.paymentlist.PaymentListService;
import com.azry.sps.server.services.paymentlist.PaymentListManager;
import com.azry.sps.server.services.service.ServiceManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("sps/servlet/PaymentList")
public class PaymentListServiceImpl extends RemoteServiceServlet implements PaymentListService {

	@Inject
	PaymentListManager paymentListManager;

	@Inject
	ServiceManager serviceManager;

	@Override
	public PaymentListDTO getPaymentList(String personalNumber) {
		return PaymentListDTO.toDTO(paymentListManager.getPaymentList(personalNumber));
	}

	@Override
	public PaymentListDTO getPaymentListWithServices(String personalNumber) {
		PaymentListDTO paymentList =  getPaymentList(personalNumber);
		List<PaymentListEntryDTO> paymentListEntries = new ArrayList<>();
		for (PaymentListEntryDTO entry : paymentList.getEntries()) {
			Service service = serviceManager.getService(entry.getServiceId());
			if (service == null) {
				continue;
			}
			if (service.isActive()) {
				entry.setService(ServiceDTO.toDTO(service));
				paymentListEntries.add(entry);
			}
		}
		paymentList.setEntries(paymentListEntries);
		return paymentList;
	}

	@Override
	public PaymentListEntryDTO addPaymentListEntry(ClientDTO client, PaymentListEntryDTO paymentListEntry) {
			PaymentListEntry paymentList = paymentListManager.addPaymentListEntry(client.entityFromDTO(), paymentListEntry.fromDTO());
			return PaymentListEntryDTO.toDTO(paymentList);
	}

	@Override
	public void deletePaymentListEntry(long id) {
		paymentListManager.deletePaymentListEntry(id);
	}
}

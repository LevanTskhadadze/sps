package com.azry.sps.console.server;

import com.azry.sps.console.shared.bankintegration.BankIntegrationService;
import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.bankclient.ClientDTO;
import com.azry.sps.console.shared.paymentlist.PaymentListService;
import com.azry.sps.fi.model.exception.FIConnectivityException;
import com.azry.sps.fi.model.exception.FiException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;


@WebServlet("sps/servlet/BankIntegration")
public class BankIntegrationServiceImpl extends RemoteServiceServlet implements BankIntegrationService {

	@Inject
	com.azry.sps.fi.service.BankIntegrationService bi;

	@Inject
	PaymentListService paymentListService;

	@Override
	public ClientDTO getClientWithAccount(String personalNumber) throws SPSConsoleException {
		try {
			return ClientDTO.bankClientToDTO(bi.getClientWithAccount(personalNumber));
		} catch (FiException ex) {
			throw new SPSConsoleException(ex.getCode());
		} catch (FIConnectivityException ex) {
			throw new SPSConsoleException("bankConnectionError");
		}
	}

	@Override
	public ClientDTO getBankClientWithPaymentList(String personalNumber) throws SPSConsoleException{
			ClientDTO clientDTO = getClientWithAccount(personalNumber);
			clientDTO.setPaymentListDTO(paymentListService.getPaymentListWithServices(personalNumber));
			return clientDTO;
	}
}

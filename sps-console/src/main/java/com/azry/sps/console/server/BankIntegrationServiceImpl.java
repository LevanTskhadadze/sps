package com.azry.sps.console.server;

import com.azry.sps.common.exceptions.SPSException;
import com.azry.sps.console.shared.bankintegration.BankIntegrationService;
import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.bankclient.AccountDTO;
import com.azry.sps.console.shared.dto.bankclient.ClientDTO;
import com.azry.sps.fi.service.BankIntegrationManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import java.util.List;


@WebServlet("sps/servlet/BankIntegration")
public class BankIntegrationServiceImpl extends RemoteServiceServlet implements BankIntegrationService {

	@Inject
	BankIntegrationManager bi;

	@Override
	public ClientDTO findClient(String personalNumber) throws SPSConsoleException {
		try {
			return ClientDTO.toDTO(bi.findClient(personalNumber));
		} catch (SPSException e) {
			throw new SPSConsoleException(e.getMessage());
		}
	}

	@Override
	public List<AccountDTO> getClientAccounts(long clientId) {
		return AccountDTO.toDTOs(bi.getClientAccounts(clientId));
	}
}

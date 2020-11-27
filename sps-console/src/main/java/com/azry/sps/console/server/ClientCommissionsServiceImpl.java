package com.azry.sps.console.server;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.exception.SPSException;
import com.azry.sps.common.model.commission.ClientCommissions;
import com.azry.sps.console.shared.clientcommission.ClientCommissionsService;
import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.commission.clientcommission.ClientCommissionsDTO;
import com.azry.sps.server.services.clientcommission.ClientCommissionsManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;

@WebServlet("sps/servlet/ClientCommissions")
public class ClientCommissionsServiceImpl extends RemoteServiceServlet implements ClientCommissionsService {

	@Inject
	ClientCommissionsManager clientCommissionsManager;



	@Override
	public PagingLoadResult<ClientCommissionsDTO> getFilteredClientCommissions(String serviceId, String channelId, PagingLoadConfig config) {
		ListResult<ClientCommissions> listResult =  clientCommissionsManager.getFilteredClientCommissions(serviceId, channelId, config.getOffset(), config.getLimit());
		return new  PagingLoadResultBean<>(ClientCommissionsDTO.toDTOs(listResult.getResultList()), listResult.getResultCount(), config.getOffset());
	}

	@Override
	public ClientCommissionsDTO getClientCommission(long serviceId) {
		return ClientCommissionsDTO.toDTO(clientCommissionsManager.getClientCommissionByServiceId(serviceId));
	}

	@Override
	public ClientCommissionsDTO updateClientCommissions(ClientCommissionsDTO clientCommissions) throws SPSConsoleException {
		try {
			return ClientCommissionsDTO.toDTO(clientCommissionsManager.updateClientCommissions(clientCommissions.fromDTO()));
		}
		catch (SPSException ex) {
			throw new SPSConsoleException(ex);
		}
	}

	@Override
	public void deleteClientCommissions(long id) {
		clientCommissionsManager.deleteClientCommissions(id);
	}
}

package com.azry.sps.api;

import com.azry.sps.api.dto.GetInfoRequest;
import com.azry.sps.api.dto.GetInfoResponse;
import com.azry.sps.api.dto.GetInfoStatus;
import com.azry.sps.common.exception.SPSException;
import com.azry.sps.common.model.service.Service;
import com.azry.sps.integration.sp.ServiceProviderIntegrationService;
import com.azry.sps.integration.sp.dto.AbonentInfo;
import com.azry.sps.integration.sp.exception.SpIntegrationException;
import com.azry.sps.server.services.service.ServiceManager;

import javax.inject.Inject;
import javax.jws.WebService;

@WebService(endpointInterface = "com.azry.sps.api.SpsApi")
public class SpsApiImpl implements SpsApi{

	@Inject
	ServiceProviderIntegrationService serviceProviderIntegrationService;

	@Inject
	ServiceManager serviceManager;


	@Override
	public GetInfoResponse getInfo(GetInfoRequest request) {
		GetInfoResponse response = new GetInfoResponse();
		Service svc = serviceManager.getService(request.getServiceId());

		if (svc == null) {
			response.setStatus(GetInfoStatus.SERVICE_NOT_FOUND);
			response.setStatusMessage("");
			return response;
		}


		try {
			AbonentInfo abonentInfo = serviceProviderIntegrationService.getInfo(svc.getServiceDebtCode(), request.getAbonentCode());
			response.setStatus(GetInfoStatus.SUCCESS);
			response.setStatusMessage("");
			response.setDebt(abonentInfo.getDebt());
			response.setInfoMessage(abonentInfo.getMessage());
		} catch (SpIntegrationException ex) {
			response.setStatus(GetInfoStatus.CONNECTION_FAILED);
			ex.printStackTrace();
		}

		return null;
	}
}

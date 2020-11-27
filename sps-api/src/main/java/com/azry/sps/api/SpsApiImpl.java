package com.azry.sps.api;

import com.azry.sps.api.dto.GetInfoRequest;
import com.azry.sps.api.dto.GetInfoResponse;

import javax.inject.Inject;
import javax.jws.WebService;

@WebService(endpointInterface = "com.azry.sps.api.SpsApi")
public class SpsApiImpl implements SpsApi{



	@Override
	public GetInfoResponse getInfo(GetInfoRequest request) {
		GetInfoResponse response = new GetInfoResponse();

		return null;
	}
}

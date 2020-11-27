package com.azry.sps.api;

import com.azry.sps.api.dto.GetInfoRequest;
import com.azry.sps.api.dto.GetInfoResponse;

import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

@WebService
public interface SpsApi {

	public GetInfoResponse getInfo(@XmlElement(name = "GetInfoRequest")GetInfoRequest request);

}

package com.azry.sps.integration.sp;

import com.azry.sps.integration.sp.dto.AbonentInfo;
import com.azry.sps.integration.sp.dto.AbonentRequest;
import com.azry.sps.integration.sp.dto.PaymentDto;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/payment-gateway")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ServicesInterface {
	@POST
	@Path("/get-info")
	AbonentInfo getAbonent(AbonentRequest info);

	@POST
	@Path("/get-info")
	Response pay(PaymentDto dto);

}

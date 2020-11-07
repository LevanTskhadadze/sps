package com.azry.sps.integration.sp;

import com.azry.sps.integration.sp.dto.AbonentDto;
import com.azry.sps.integration.sp.dto.AbonentInfoDto;
import com.azry.sps.integration.sp.dto.PaymentDto;
import com.azry.sps.systemparameters.model.sysparam.Parameter;
import com.azry.sps.systemparameters.model.sysparam.SysParam;
import com.azry.sps.systemparameters.model.SystemParameterType;
import com.azry.sps.systemparameters.model.sysparam.SystemParametersProducer;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.math.BigDecimal;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@DependsOn("sysParamProducer")
public class ServiceProviderConnector {

	@Inject
	@SysParam(type = SystemParameterType.STRING, code = "sp-integration-url", defaultValue = "http://localhost:9006")
	private Parameter<String> path;

	ResteasyClient client;
	ResteasyWebTarget target;
	ServicesInterface proxy;

	@PostConstruct
	public void startup() {
		client = new ResteasyClientBuilder().build();
		target = client.target(UriBuilder.fromPath(path.getValue()));
		proxy = target.proxy(ServicesInterface.class);
	}

	public synchronized AbonentInfoDto getInfo(String serviceCode, long id) {

		AbonentInfoDto abonentInfo = proxy.getAbonent(new AbonentDto(serviceCode, String.valueOf(id)));
		return abonentInfo;
	}

	public synchronized boolean pay(String serviceCode, String agentPaymentId, String abonentCode, BigDecimal amount) {
		Response payment = proxy.pay(new PaymentDto(serviceCode, agentPaymentId, abonentCode, amount));
		System.out.println("HTTP code: " + payment.getStatus());
		return true;
	}
}

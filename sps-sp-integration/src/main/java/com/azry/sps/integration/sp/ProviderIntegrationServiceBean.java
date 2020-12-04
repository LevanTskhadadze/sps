package com.azry.sps.integration.sp;

import com.azry.sps.integration.sp.dto.AbonentInfo;
import com.azry.sps.integration.sp.dto.AbonentRequest;
import com.azry.sps.integration.sp.dto.FailureResponse;
import com.azry.sps.integration.sp.dto.PayResponse;
import com.azry.sps.integration.sp.dto.PaymentDTO;
import com.azry.sps.integration.sp.exception.SpConnectivityException;
import com.azry.sps.integration.sp.exception.SpIntegrationException;
import com.azry.sps.integration.sp.inteceptor.SpExceptionInterceptor;
import com.azry.sps.systemparameters.model.SystemParameterType;
import com.azry.sps.systemparameters.sysparam.Parameter;
import com.azry.sps.systemparameters.sysparam.SysParam;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.DependsOn;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Stateless(name = "SpIntegrationService")
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@DependsOn("SystemParametersProducer")
@Interceptors(SpExceptionInterceptor.class)
public class ProviderIntegrationServiceBean implements ProviderIntegrationService {

	@Inject
	@SysParam(type = SystemParameterType.STRING, code = "spIntegrationUrl", defaultValue = "http://localhost:9006")
	private Parameter<String> path;

	@Inject
	@SysParam(type = SystemParameterType.INTEGER, code = "spReadTimeout", defaultValue = "10")
	private Parameter<Integer> readTimeout;

	@Inject
	@SysParam(type = SystemParameterType.INTEGER, code = "spConnectTimeout", defaultValue = "10")
	private Parameter<Integer> connectTimeout;

	ResteasyClient client;


	@PostConstruct
	public void startup() {
		client = new ResteasyClientBuilder()
			.readTimeout(readTimeout.getValue(), TimeUnit.SECONDS)
			.connectTimeout(connectTimeout.getValue(), TimeUnit.SECONDS)
			.build();
	}

	private ServicesInterface getProxy() {
		ResteasyWebTarget target = client.target(UriBuilder.fromPath(path.getValue()));;
		return target.proxy(ServicesInterface.class);
	}

	public AbonentInfo getInfo(String serviceCode, String abonentCode) throws SpConnectivityException {
			return getProxy().getAbonent(new AbonentRequest("gateway-" + serviceCode, abonentCode));
	}

	public PayResponse pay(String serviceCode, String agentPaymentId, String abonentCode, BigDecimal amount) throws SpIntegrationException, SpConnectivityException {
			Response response = getProxy().pay(new PaymentDTO("gateway-" + serviceCode,
				String.valueOf(agentPaymentId),
				abonentCode,
				amount));
			if (response.getStatusInfo().equals(Response.Status.BAD_REQUEST)) {
				FailureResponse failureResponse = response.readEntity(FailureResponse.class);
					throw new SpIntegrationException(failureResponse.getMessage(), failureResponse.getStatus());
			}
			return response.readEntity(PayResponse.class);
	}
}

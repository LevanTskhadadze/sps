package com.azry.sps.integration.sp;

import com.azry.sps.common.exception.SPSException;
import com.azry.sps.integration.sp.dto.AbonentInfo;
import com.azry.sps.integration.sp.dto.AbonentRequest;
import com.azry.sps.integration.sp.dto.PayResponse;
import com.azry.sps.integration.sp.dto.PaymentDTO;
import com.azry.sps.systemparameters.model.SystemParameterType;
import com.azry.sps.systemparameters.model.sysparam.Parameter;
import com.azry.sps.systemparameters.model.sysparam.SysParam;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.UriBuilder;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Singleton(name = "SpIntegrationService")
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@DependsOn("SystemParametersProducer")
public class ServiceProviderIntegrationServiceBean implements ServiceProviderIntegrationService {

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

	public AbonentInfo getInfo(String serviceCode, String id) throws SPSException {


		try {
			return getProxy().getAbonent(new AbonentRequest("gateway-" + serviceCode, id));
		} catch (Exception ex) {
			throw new SPSException("SPConnectionFailed", ex);
		}
	}

	public PayResponse pay(String serviceCode, long agentPaymentId, String abonentCode, BigDecimal amount) throws SPSException {
		try {
			PayResponse payment = getProxy().pay(new PaymentDTO(serviceCode,
				String.valueOf(agentPaymentId),
				abonentCode,
				amount)).readEntity(PayResponse.class);
			return payment;
		} catch (Exception ex) {
			if (ex instanceof ProcessingException) {
				throw new SPSException("spBadRequest", ex);
			}
			throw new SPSException("spConnectionFailed", ex);
		}
	}
}

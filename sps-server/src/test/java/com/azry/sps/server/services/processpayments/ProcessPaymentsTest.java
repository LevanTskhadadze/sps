package com.azry.sps.server.services.processpayments;

import com.azry.sps.common.model.client.Client;
import com.azry.sps.common.model.payment.Payment;
import com.azry.sps.common.model.payment.PaymentStatus;
import com.azry.sps.common.model.service.Service;
import com.azry.sps.integration.sp.ProviderIntegrationService;
import com.azry.sps.integration.sp.dto.PayResponse;
import com.azry.sps.integration.sp.dto.SpResponseStatus;
import com.azry.sps.integration.sp.exception.SpConnectivityException;
import com.azry.sps.integration.sp.exception.SpIntegrationException;
import com.azry.sps.server.services.payment.PaymentManager;
import com.azry.sps.server.services.service.ServiceManager;
import com.azry.sps.systemparameters.sysparam.Parameter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class ProcessPaymentsTest {

	@Mock
	private PaymentManager paymentManager;

	@Mock
	private ServiceManager serviceManager;

	@Mock
	private ProviderIntegrationService bankIntegrationService;

	PayResponse successfulResponse;

	PayResponse unsuccessfulResponse;

	private static final Integer waitAmountHours = 1;

	@Mock
	private Parameter<Integer> maxPaymentPendingHours;

	@InjectMocks
	ProcessPayments processPayments;

	Payment payment;

	Service service;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		initObjects();

		doNothing().when(paymentManager).updatePayment(payment);
		doNothing().when(paymentManager).addPaymentStatusLog(any());
		when(maxPaymentPendingHours.getValue()).thenReturn(waitAmountHours);
		when(serviceManager.getService(anyLong())).thenReturn(service);

	}

	@Test
	public void testSuccessfulProcessPayment() throws SpConnectivityException, SpIntegrationException {
		when(bankIntegrationService.pay(anyString(), anyString(), anyString(), anyObject())).thenReturn(successfulResponse);

		processPayments.processPayment(payment);
		assertSame(payment.getStatus(), PaymentStatus.PERFORMED);

	}

	@Test
	public void testRejectedProcessPayment() throws SpConnectivityException, SpIntegrationException {
		when(bankIntegrationService.pay(anyString(), anyString(), anyString(), anyObject())).thenReturn(unsuccessfulResponse);

		processPayments.processPayment(payment);
		assertSame(payment.getStatus(), PaymentStatus.REJECTED);

	}

	@Test
	public void testBankConnectivityError() throws SpConnectivityException, SpIntegrationException {
		when(bankIntegrationService.pay(anyString(), anyString(), anyString(), anyObject())).thenThrow(new SpConnectivityException(null, new Exception()));

		processPayments.processPayment(payment);
		assertSame(payment.getStatus(), PaymentStatus.PENDING);

		payment.setCreateTime(Date.from(new Date().toInstant().minus(Duration.ofHours(waitAmountHours))));

		processPayments.processPayment(payment);
		assertSame(payment.getStatus(), PaymentStatus.REJECTED);
	}

	@Test
	public void testBadRequest() throws SpConnectivityException, SpIntegrationException {
		when(bankIntegrationService.pay(anyString(), anyString(), anyString(), anyObject())).thenThrow(new SpIntegrationException(null));

		processPayments.processPayment(payment);
		assertSame(payment.getStatus(), PaymentStatus.REJECTED);

	}

	private void initObjects() {
		payment = new Payment();

		service = new Service();

		Client client = new Client();
		client.setPersonalNumber("12345678912");
		payment.setClient(client);
		payment.setId(1);
		payment.setAbonentCode("test");
		payment.setCreateTime(new Date());
		payment.setServiceId(5);
		payment.setStatus(PaymentStatus.COLLECTED);
		payment.setAmount(new BigDecimal(50));

		successfulResponse = new PayResponse();
		successfulResponse.setStatus(SpResponseStatus.SUCCESS);

		unsuccessfulResponse = new PayResponse();
		unsuccessfulResponse.setStatus(SpResponseStatus.REJECT);


	}

}

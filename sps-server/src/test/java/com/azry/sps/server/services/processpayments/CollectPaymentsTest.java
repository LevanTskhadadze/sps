package com.azry.sps.server.services.processpayments;

import com.azry.sps.common.model.channels.Channel;
import com.azry.sps.common.model.channels.FiServiceUnavailabilityAction;
import com.azry.sps.common.model.client.Client;
import com.azry.sps.common.model.payment.Payment;
import com.azry.sps.common.model.payment.PaymentStatus;
import com.azry.sps.common.model.transaction.TransactionOrder;
import com.azry.sps.common.model.transaction.TransactionType;
import com.azry.sps.fi.model.exception.FIConnectivityException;
import com.azry.sps.fi.model.exception.FIException;
import com.azry.sps.fi.model.transaction.FiTransactionRequest;
import com.azry.sps.fi.model.transaction.FiTransactionResponse;
import com.azry.sps.fi.model.transaction.FiTransactionResponseEntry;
import com.azry.sps.fi.service.BankIntegrationService;
import com.azry.sps.server.services.channel.ChannelManager;
import com.azry.sps.server.services.payment.PaymentManager;
import com.azry.sps.server.services.transactionorder.TransactionOrderManager;
import com.azry.sps.systemparameters.sysparam.Parameter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class CollectPaymentsTest {

	@Mock
	PaymentManager paymentManager;

	@Mock
	TransactionOrderManager transactionOrderManager;

	@Mock
	BankIntegrationService bankIntegrationService;

	@Mock
	ChannelManager channelManager;

	@Mock
	Parameter<Integer> maxPaymentPendingHours;

	@InjectMocks
	CollectPayments collectPayments;

	Payment payment;

	Channel channel;

	TransactionOrder transactionOrder;

	FiTransactionResponse fiTransactionResponse;

	FiTransactionResponseEntry fiTransactionResponseEntryPrincipal;

	FiTransactionResponseEntry fiTransactionResponseEntryCommission;

	@Before
	public void create() throws FIException, FIConnectivityException {
		initObjects();
		MockitoAnnotations.initMocks(this);

		doNothing().when(paymentManager).updatePayment(payment);
		doNothing().when(paymentManager).addPaymentStatusLog(any());
		doNothing().when(transactionOrderManager).changeTransactions(any());
		when(transactionOrderManager.getTransaction(anyLong(), isA(TransactionType.class))).thenReturn(transactionOrder);
		when(channelManager.getChannel(anyLong())).thenReturn(channel);
		when(maxPaymentPendingHours
			.getValue()).thenReturn(1);
	}

	@Test
	public void processPayment_Should_SetPaymentStatusToCOLLECTED() throws FIException, FIConnectivityException {
		when(bankIntegrationService.processTransactions(isA(FiTransactionRequest.class))).thenReturn(fiTransactionResponse);

		collectPayments.processPayment(payment);

		assertThat(payment.getStatus(), equalTo(PaymentStatus.COLLECTED));
	}

	@Test
	public void processPayment_Should_SetPaymentStatusToCOLLECT_REJECTED_When_FIExceptionIsThrown() throws FIException, FIConnectivityException {
		when(bankIntegrationService.processTransactions(isA(FiTransactionRequest.class))).thenThrow(new FIException());

		collectPayments.processPayment(payment);

		assertThat(payment.getStatus(), equalTo(PaymentStatus.COLLECT_REJECTED));
	}

	/*
	If channel.getFiServiceUnavailabilityAction() ==  FiServiceUnavailabilityAction.WAIT than payment status should
	be set to PaymentStatus.COLLECT_PENDING until paymentWaitingTime(System Parameter) expires;
	*/
	@Test
	public void processPayment_Should_SetPaymentStatusToCOLLECT_PENDING_When_FIConnectivityExceptionIsThrown() throws FIException, FIConnectivityException {
		when(bankIntegrationService.processTransactions(isA(FiTransactionRequest.class))).thenThrow(new FIConnectivityException());

		collectPayments.processPayment(payment);

		assertThat(payment.getStatus(), equalTo(PaymentStatus.COLLECT_PENDING));
	}

	/*
	Payment status should be rejected immediately if channel.getFiServiceUnavailabilityAction() == FiServiceUnavailabilityAction.REJECT_IMMEDIATELY.
	*/
	@Test
	public void processPayment_Should_SetPaymentStatusToCOLLECT_REJECTED_When_FIConnectivityExceptionIsThrown() throws FIException, FIConnectivityException {
		when(bankIntegrationService.processTransactions(isA(FiTransactionRequest.class))).thenThrow(new FIConnectivityException());
		channel.setFiServiceUnavailabilityAction(FiServiceUnavailabilityAction.REJECT_IMMEDIATELY);

		collectPayments.processPayment(payment);

		assertThat(payment.getStatus(), equalTo(PaymentStatus.COLLECT_REJECTED));
	}

	/*
	If channel.getFiServiceUnavailabilityAction() ==  FiServiceUnavailabilityAction.WAIT than payment should be rejected after maxPaymentPendingHours has elapsed;
	*/
	@Test
	public void processPayment_Should_SetPaymentStatusToCOLLECT_REJECTED_when_FIConnectivityExceptionIsThrown() throws FIException, FIConnectivityException {
		when(bankIntegrationService.processTransactions(isA(FiTransactionRequest.class))).thenThrow(new FIConnectivityException());
		channel.setFiServiceUnavailabilityAction(FiServiceUnavailabilityAction.WAIT);
		payment.setCreateTime(Date.from(Instant.now().minus(Duration.ofHours(maxPaymentPendingHours.getValue()+1))));

		collectPayments.processPayment(payment);

		assertThat(payment.getStatus(), equalTo(PaymentStatus.COLLECT_REJECTED));
	}


	private void initObjects() {

		collectPayments = new CollectPayments();

		fiTransactionResponseEntryPrincipal = new FiTransactionResponseEntry();
		fiTransactionResponseEntryPrincipal.setTransactionId("1");
		fiTransactionResponseEntryPrincipal.setFiId("1");

		fiTransactionResponseEntryCommission = new FiTransactionResponseEntry();
		fiTransactionResponseEntryCommission.setTransactionId("2");
		fiTransactionResponseEntryCommission.setFiId("2");

		fiTransactionResponse = new FiTransactionResponse();
		fiTransactionResponse.getTransactionResponseEntries().add(fiTransactionResponseEntryPrincipal);
		fiTransactionResponse.getTransactionResponseEntries().add(fiTransactionResponseEntryCommission);

		channel = new Channel();

		payment = new Payment();
		Client client = new Client();
		client.setPersonalNumber("12345678912");
		payment.setClient(client);
		payment.setId(1);
		payment.setAbonentCode("test");
		payment.setCreateTime(new Date());
		payment.setServiceId(5);
		payment.setAmount(new BigDecimal(50));

		transactionOrder = new TransactionOrder();
		transactionOrder.setAmount(new BigDecimal(50));
	}
}
package com.azry.sps.server.services.processpayments;

import com.azry.sps.common.model.channels.Channel;
import com.azry.sps.common.model.channels.FiServiceUnavailabilityAction;
import com.azry.sps.common.model.payment.Payment;
import com.azry.sps.common.model.payment.PaymentStatus;
import com.azry.sps.common.model.payment.PaymentStatusLog;
import com.azry.sps.common.model.transaction.TransactionOrder;
import com.azry.sps.common.model.transaction.TransactionType;
import com.azry.sps.fi.model.exception.FIConnectivityException;
import com.azry.sps.fi.model.exception.FIException;
import com.azry.sps.fi.model.transaction.FiTransaction;
import com.azry.sps.fi.model.transaction.FiTransactionRequest;
import com.azry.sps.fi.model.transaction.FiTransactionResponse;
import com.azry.sps.fi.service.BankIntegrationService;
import com.azry.sps.server.services.channel.ChannelManager;
import com.azry.sps.server.services.payment.PaymentManager;
import com.azry.sps.server.services.payment.PaymentParams;
import com.azry.sps.server.services.transactionorder.TransactionOrderManager;
import com.azry.sps.systemparameters.model.SystemParameterType;
import com.azry.sps.systemparameters.sysparam.Parameter;
import com.azry.sps.systemparameters.sysparam.SysParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Singleton(name = "CollectPayments")
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class CollectPayments {

	private static final List<PaymentStatus> TARGET_STATUSES = Arrays.asList(PaymentStatus.CREATED, PaymentStatus.COLLECT_PENDING);

	private final Logger log = LoggerFactory.getLogger(CollectPayments.class);

	@EJB
	CollectPayments self;

	@Inject
	@SysParam(code = "paymentWaitingTime", type = SystemParameterType.INTEGER)
	Parameter<Integer> paymentWaitingTime;

	@Resource
	TimerService timerService;

	@Inject
	PaymentManager paymentManager;

	@Inject
	TransactionOrderManager transactionOrderManager;

	@Inject
	BankIntegrationService bankIntegrationService;

	@Inject
	ChannelManager channelManager;

	@PostConstruct
	public void startup() {
		timerService.createIntervalTimer(15000, 30 * 1000, new TimerConfig(null, false));
	}

	private void setPaymentStatus(Payment payment, String message, PaymentStatus status) {
		payment.setStatus(status);
		PaymentStatusLog newStatus = new PaymentStatusLog();

		newStatus.setPaymentId(payment.getId());
		newStatus.setStatus(status);
		newStatus.setStatusMessage(message);
		newStatus.setStatusTime(new Date());

		paymentManager.updatePayment(payment);
		paymentManager.addPaymentStatusLog(newStatus);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void processPayment(Payment payment) {
		try {
			TransactionOrder principalTransactionOrder = transactionOrderManager.getTransaction(payment.getId(), TransactionType.PRINCIPAL_AMOUNT);
			TransactionOrder clCommissionTransactionOrder = transactionOrderManager.getTransaction(payment.getId(), TransactionType.CLIENT_COMMISSION_AMOUNT);

			if (principalTransactionOrder == null) {
				setPaymentStatus(payment, "Transaction not found", PaymentStatus.COLLECT_REJECTED);
				return;
			}

			FiTransactionRequest request = new FiTransactionRequest();

			request.getTransactions().add(new FiTransaction(principalTransactionOrder, payment.getClient().getPersonalNumber()));
			if (clCommissionTransactionOrder != null) {
				request.getTransactions().add(new FiTransaction(clCommissionTransactionOrder, payment.getClient().getPersonalNumber()));
			}

			FiTransactionResponse response = bankIntegrationService.processTransactions(request);

			principalTransactionOrder.setFild(response.getTransactionResponseEntries().get(0).getFiId());
			if (clCommissionTransactionOrder != null) {
				clCommissionTransactionOrder.setFild(response.getTransactionResponseEntries().get(1).getFiId());
			}

			transactionOrderManager.changeTransactions(Arrays.asList(principalTransactionOrder, clCommissionTransactionOrder));
			setPaymentStatus(payment, null, PaymentStatus.COLLECTED);
		} catch (FIException ex) {
			setPaymentStatus(payment, ex.getMessage(), PaymentStatus.COLLECT_REJECTED);
		} catch (FIConnectivityException ex) {
			Channel channel = channelManager.getChannel(payment.getChannelId());
			if (channel.getFiServiceUnavailabilityAction() == FiServiceUnavailabilityAction.REJECT_IMMEDIATELY) {
				setPaymentStatus(payment, ex.getMessage(), PaymentStatus.COLLECT_REJECTED);
			} else {
				long diffInHours = (new Date().getTime() - payment.getCreateTime().getTime()) / 1000 / 60 / 60;

				if (diffInHours >= paymentWaitingTime.getValue()) {
					setPaymentStatus(payment, ex.getMessage(), PaymentStatus.COLLECT_REJECTED);
				} else {
					if (payment.getStatus() != PaymentStatus.COLLECT_PENDING) {
						setPaymentStatus(payment, ex.getMessage(), PaymentStatus.COLLECT_PENDING);
					}
				}
			}
		}
	}

	@Timeout
	public void process() {
		List<Payment> payments = paymentManager.getPayments(0, Integer.MAX_VALUE, new PaymentParams(), TARGET_STATUSES)
			.getResultList();

		for (Payment payment : payments) {
			try {
				self.processPayment(payment);
			} catch (Exception ex) {
				log.info("Unexpected exception occurred while processing the first stage of payments:" + ex.getMessage());
				setPaymentStatus(payment, "unexpected exception", PaymentStatus.COLLECT_REJECTED);

			}
		}
	}
}

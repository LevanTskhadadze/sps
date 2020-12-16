package com.azry.sps.server.services.processpayments;

import com.azry.sps.common.exception.SPSException;
import com.azry.sps.common.model.payment.Payment;
import com.azry.sps.common.model.payment.PaymentStatus;
import com.azry.sps.common.model.payment.PaymentStatusLog;
import com.azry.sps.common.model.service.Service;
import com.azry.sps.integration.sp.ProviderIntegrationService;
import com.azry.sps.integration.sp.dto.PayResponse;
import com.azry.sps.integration.sp.dto.SpResponseStatus;
import com.azry.sps.integration.sp.exception.SpConnectivityException;
import com.azry.sps.integration.sp.exception.SpIntegrationException;
import com.azry.sps.server.services.payment.PaymentManager;
import com.azry.sps.server.services.service.ServiceManager;
import com.azry.sps.systemparameters.model.SystemParameterType;
import com.azry.sps.systemparameters.sysparam.Parameter;
import com.azry.sps.systemparameters.sysparam.SysParam;
import lombok.extern.slf4j.Slf4j;

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
import java.time.Duration;
import java.util.Date;
import java.util.List;

@Singleton(name = "ProcessPayments")
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Slf4j
public class ProcessPayments {

	@EJB
	ProcessPayments self;

	@Inject
	private PaymentManager paymentManager;

	@Inject
	private ServiceManager serviceManager;

	@Inject
	private ProviderIntegrationService bankIntegrationService;

	@Resource
	private TimerService timerService;

	@Inject
	@SysParam(type = SystemParameterType.INTEGER, code = "processPaymentsInterval", defaultValue = "30")
	private Parameter<Integer> processPaymentsInterval;

	@Inject
	@SysParam(type = SystemParameterType.INTEGER, code = "maxPaymentPendingHours", defaultValue = "48")
	private Parameter<Integer> maxPaymentPendingHours;

	@PostConstruct
	public void startup() {
		if (isProcessPaymentEnabled()) {
			timerService.createIntervalTimer(30000, processPaymentsInterval.getValue() * 1000, new TimerConfig(null, false));
		}
	}

	@Timeout
	private void processPayments() {
		List<Payment> payments = paymentManager.getCollectedAndPendingPayments();
		for (Payment payment : payments) {
			try {
				self.processPayment(payment);
			} catch (Exception ex) {
				setPaymentStatus(payment, PaymentStatus.REJECTED,
					"Unexpected error occurred during Payment processing. Payment Id: " + payment.getId());
				log.error("Unexpected error occurred during Payment processing. Payment Id: " + payment.getId(), ex);
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void processPayment(Payment payment) {
		Service service = serviceManager.getService(payment.getServiceId());
		if (service == null) {
			setPaymentStatus(payment, PaymentStatus.REJECTED, "Payment rejected. Service not found");
			return;
		}
		try {
			PayResponse payResponse = bankIntegrationService.pay(service.getServicePayCode(),
				String.valueOf(payment.getId()),
				payment.getAbonentCode(),
				payment.getAmount());
			if (payResponse.getStatus().equals(SpResponseStatus.SUCCESS)) {
				setPaymentStatus(payment, PaymentStatus.PERFORMED, null);
			} else if (payResponse.getStatus().equals(SpResponseStatus.REJECT)) {
				setPaymentStatus(payment, PaymentStatus.REJECTED, payResponse.getMessage());
			}
		} catch (SpIntegrationException ex) {
			setPaymentStatus(payment, PaymentStatus.REJECTED, ex.getMessage());
			log.error("Payment Rejected. Id: " + payment.getId() ,ex);
		} catch (SpConnectivityException ex) {
			Date currentDateTime = new Date();
			if (currentDateTime.before(Date.from(payment.getCreateTime().toInstant().plus(Duration.ofHours(maxPaymentPendingHours.getValue()))))) {
				setPaymentStatus(payment, PaymentStatus.PENDING, ex.getMessage());
				log.error("Payment Pending. Id: " + payment.getId() ,ex);
			} else {
				setPaymentStatus(payment, PaymentStatus.REJECTED, "Payment rejected. Max pending time elapsed");
				log.error("Payment Rejected. Id: " + payment.getId() ,ex);
			}
		}
	}

	public synchronized Payment retryPayment(Payment payment) throws SPSException {
		Payment paymentEntity = paymentManager.getPayment(payment.getId());
		if (paymentManager.getPayment(payment.getId()).getStatus().equals(PaymentStatus.REJECTED)) {
			try {
				processPayment(paymentEntity);
			} catch (Exception ex) {
				setPaymentStatus(payment, PaymentStatus.REJECTED,
					"Unexpected error occurred during Payment processing. Payment Id: " + payment.getId());
				log.error("Unexpected error occurred during Payment processing. Payment Id: " + payment.getId(), ex);
			} finally {
				return paymentEntity;
			}
		} else {
			return paymentEntity;
		}
	}

	private void setPaymentStatus(Payment payment, PaymentStatus paymentStatus, String statusMessage) {
		PaymentStatusLog paymentStatusLog = new PaymentStatusLog();
		payment.setStatus(paymentStatus);
		paymentStatusLog.setPaymentId(payment.getId());
		paymentStatusLog.setStatus(paymentStatus);
		if (statusMessage != null) {
			payment.setStatusMessage(statusMessage);
			paymentStatusLog.setStatusMessage(statusMessage);
		}
		paymentManager.updatePayment(payment);
		paymentManager.addPaymentStatusLog(paymentStatusLog);
	}

	private boolean isProcessPaymentEnabled() {
		String value = System.getProperty("StartProcessPayment");
		return Boolean.parseBoolean(value);
	}
}



package com.azry.sps.server.services.processpayments;

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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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

@Singleton(name = "ProcessPaymentService")
@Startup
public class ProcessPaymentService {

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
		timerService.createIntervalTimer(50000, processPaymentsInterval.getValue() * 1000, new TimerConfig(null, false));
	}

	@Timeout
	private void processPayments() {
		List<Payment> payments = paymentManager.getCollectedAndPendingPayments();
		for (Payment payment : payments) {
			processPayment(payment);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void processPayment(Payment payment) {
		Service service = serviceManager.getService(payment.getServiceId());
		PaymentStatusLog paymentStatusLog = new PaymentStatusLog();
		try {
			PayResponse payResponse = bankIntegrationService.pay(service.getServicePayCode(),
				payment.getId(),
				payment.getAbonentCode(),
				payment.getAmount());
			if (payResponse.getStatus().equals(SpResponseStatus.SUCCESS)) {
				payment.setStatus(PaymentStatus.PERFORMED);
				paymentStatusLog.setPaymentId(payment.getId());
				paymentStatusLog.setStatus(PaymentStatus.PERFORMED);
			} else if (payResponse.getStatus().equals(SpResponseStatus.REJECT)) {
				payment.setStatus(PaymentStatus.REJECTED);
				paymentStatusLog.setPaymentId(payment.getId());
				paymentStatusLog.setStatus(PaymentStatus.REJECTED);
				paymentStatusLog.setStatusMessage(payResponse.getMessage());
			}
		} catch (SpIntegrationException ex) {
			payment.setStatus(PaymentStatus.REJECTED);
			paymentStatusLog.setPaymentId(payment.getId());
			paymentStatusLog.setStatus(PaymentStatus.REJECTED);
			paymentStatusLog.setStatusMessage(ex.getMessage());
		} catch (SpConnectivityException ex) {
			Date currentDateTime = new Date();
			if (currentDateTime.before(Date.from(payment.getCreateTime().toInstant().plus(Duration.ofHours(maxPaymentPendingHours.getValue()))))) {
				payment.setStatus(PaymentStatus.PENDING);
				paymentStatusLog.setPaymentId(payment.getId());
				paymentStatusLog.setStatus(PaymentStatus.PENDING);
				paymentStatusLog.setStatusMessage(ex.getMessage());
			} else {
				payment.setStatus(PaymentStatus.REJECTED);
				paymentStatusLog.setPaymentId(payment.getId());
				paymentStatusLog.setStatus(PaymentStatus.REJECTED);
				paymentStatusLog.setStatusMessage("Payment rejected. Max pending time elapsed");
			}
		} finally {
			paymentManager.updatePayment(payment);
			paymentManager.addPaymentStatusLog(paymentStatusLog);
		}
	}
}

package com.azry.sps.server.services.payment;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.model.payment.Payment;
import com.azry.sps.common.model.payment.PaymentStatus;
import com.azry.sps.common.model.payment.PaymentStatusLog;

import javax.ejb.Local;
import java.util.List;

@Local
public interface PaymentManager {

	ListResult<Payment> getPayments(int offset, int limit, PaymentParams params, List<PaymentStatus> statuses);

	Payment getPayment(long id);

	Payment getPaymentByAgentId(String agentPaymentId);

	List<Payment> getCollectedAndPendingPayments();

	List<PaymentStatusLog> getChanges(long id);

	void addPayments(List<Payment> payments);

	Payment addPayment(Payment payment);

	void updatePayment(Payment payment);

	void addPaymentStatusLog(PaymentStatusLog paymentStatusLog);

    void changePaymentStatus(long paymentId, PaymentStatus status);
}

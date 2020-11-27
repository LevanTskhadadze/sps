package com.azry.sps.server.services.payment;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.model.payment.Payment;
import com.azry.sps.common.model.payment.PaymentStatus;
import com.azry.sps.common.model.payment.PaymentStatusLog;

import javax.ejb.Local;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Local
public interface PaymentManager {

	ListResult<Payment> getPayments(int offset, int limit, Map<String, Serializable> params, List<PaymentStatus> statuses);

	Payment getPayment(long id);

	Payment getPaymentByAgentId(String agentPaymentId);

	List<PaymentStatusLog> getChanges(long id);

	void addPayments(List<Payment> payments);

}

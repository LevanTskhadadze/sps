package com.azry.sps.server.services.payment;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.model.payment.Payment;
import com.azry.sps.common.model.payment.PaymentStatus;

import javax.ejb.Local;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Local
public interface PaymentManager {

	ListResult<Payment> getPayments(int offset, int limit, Map<String, Serializable> params, List<PaymentStatus> statuses);

	void addPayments(List<Payment> payments);

}
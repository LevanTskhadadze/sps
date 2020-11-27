package com.azry.sps.console.shared.payment;

import com.azry.sps.common.model.payment.Payment;
import com.azry.sps.console.shared.dto.payment.PaymentInfoDTO;
import com.azry.sps.console.shared.dto.payment.PaymentDTO;
import com.azry.sps.console.shared.dto.payment.PaymentStatusDTO;
import com.azry.sps.console.shared.dto.payment.PaymentStatusLogDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@RemoteServiceRelativePath("servlet/payment")
public interface PaymentService extends RemoteService {

	PagingLoadResult<PaymentDTO> getPayments(int offset, int limit, Map<String, Serializable> params, List<PaymentStatusDTO> statuses);

	List<PaymentStatusLogDTO> getChanges(long paymentId);

	PaymentInfoDTO getPaymentInfo(PaymentDTO paymentDTO);

	void addPayments(List<PaymentDTO> payments);
}

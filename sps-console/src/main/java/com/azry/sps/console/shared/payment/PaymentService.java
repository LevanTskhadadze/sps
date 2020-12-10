package com.azry.sps.console.shared.payment;

import com.azry.sps.console.shared.dto.payment.PaymentDTO;
import com.azry.sps.console.shared.dto.payment.PaymentInfoDTO;
import com.azry.sps.console.shared.dto.payment.PaymentStatusDTO;
import com.azry.sps.console.shared.dto.payment.PaymentStatusLogDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import java.util.List;

@RemoteServiceRelativePath("servlet/payment")
public interface PaymentService extends RemoteService {

	PagingLoadResult<PaymentDTO> getPayments(int offset, int limit, PaymentParamDTO params, List<PaymentStatusDTO> statuses);

	List<PaymentStatusLogDTO> getChanges(long paymentId);

	PaymentInfoDTO getPaymentInfo(PaymentDTO paymentDTO);

	List<PaymentDTO> addPayments(List<PaymentDTO> payments);

	PaymentStatusDTO retryPayment(long paymentId);
}

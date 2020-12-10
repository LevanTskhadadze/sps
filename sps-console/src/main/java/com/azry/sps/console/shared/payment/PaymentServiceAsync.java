package com.azry.sps.console.shared.payment;

import com.azry.sps.console.shared.dto.payment.PaymentDTO;
import com.azry.sps.console.shared.dto.payment.PaymentInfoDTO;
import com.azry.sps.console.shared.dto.payment.PaymentStatusDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import java.util.List;

public interface PaymentServiceAsync {

	void getPayments(int offset, int limit, PaymentParamDTO params, List<PaymentStatusDTO> statuses, AsyncCallback<PagingLoadResult<PaymentDTO>> callback);

	void getChanges(long paymentId, AsyncCallback<List<PaymentDTO>> callback);

	void getPaymentInfo(PaymentDTO paymentDTO, AsyncCallback<PaymentInfoDTO> callback);

	void addPayments(List<PaymentDTO> payments, AsyncCallback<List<PaymentDTO>> async);

	void retryPayment(long paymentId, AsyncCallback<PaymentStatusDTO> callback);
}

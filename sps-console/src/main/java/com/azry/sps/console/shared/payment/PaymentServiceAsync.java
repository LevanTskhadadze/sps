package com.azry.sps.console.shared.payment;

import com.azry.sps.console.shared.dto.payment.PaymentDto;
import com.azry.sps.console.shared.dto.payment.PaymentStatusDto;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface PaymentServiceAsync {

	void getPayments(int offset, int limit, Map<String, Serializable> params, List<PaymentStatusDto> statuses, AsyncCallback<PagingLoadResult<PaymentDto>> callback);

	void addPayments(List<PaymentDto> payments, AsyncCallback<Void> async);
}

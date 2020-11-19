package com.azry.sps.console.shared.payment;

import com.azry.sps.console.shared.dto.payment.PaymentInfoDto;
import com.azry.sps.console.shared.dto.payment.PaymentDto;
import com.azry.sps.console.shared.dto.payment.PaymentStatusDto;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@RemoteServiceRelativePath("servlet/payment")
public interface PaymentService extends RemoteService {

	PagingLoadResult<PaymentDto> getPayments(int offset, int limit, Map<String, Serializable> params, List<PaymentStatusDto> statuses);

	List<PaymentDto> getChanges(String agentPaymentId);

	PaymentInfoDto getPaymentInfo(String agentPaymentId, long id);

	void addPayments(List<PaymentDto> payments);
}

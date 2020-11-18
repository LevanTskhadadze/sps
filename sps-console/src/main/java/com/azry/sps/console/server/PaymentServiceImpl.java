package com.azry.sps.console.server;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.model.payment.Payment;
import com.azry.sps.common.model.transaction.TransactionType;
import com.azry.sps.console.shared.dto.payment.PaymentInfoDto;
import com.azry.sps.console.shared.dto.payment.PaymentDto;
import com.azry.sps.console.shared.dto.payment.PaymentStatusDto;
import com.azry.sps.console.shared.dto.transactionorder.TransactionOrderDto;
import com.azry.sps.console.shared.payment.PaymentService;
import com.azry.sps.server.services.payment.PaymentManager;
import com.azry.sps.server.services.transactionorder.TransactionOrderManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@WebServlet("sps/servlet/payment")
public class PaymentServiceImpl extends RemoteServiceServlet implements PaymentService {

	@Inject
	PaymentManager paymentManager;

	@Inject
	TransactionOrderManager transactionOrderManager;

	@Override
	public PagingLoadResult<PaymentDto> getPayments(int offset, int limit, Map<String, Serializable> params, List<PaymentStatusDto> statuses) {
		ListResult<Payment> res = paymentManager.getPayments(offset, limit, params, PaymentDto.convertDtoToStatuses(statuses));

		return new PagingLoadResultBean<>(
			PaymentDto.toDtos(res.getResultList()),
			res.getResultCount(),
			offset
		);
	}

	@Override
	public List<PaymentDto> getChanges(String agentPaymentId) {
		return PaymentDto.toDtos(paymentManager.getChanges(agentPaymentId));
	}

	@Override
	public PaymentInfoDto getPaymentInfo(String agentPaymentId, long id) {
		final PaymentInfoDto dto = new PaymentInfoDto();
		dto.setChanges(getChanges(agentPaymentId));
		dto.setPrincipal(TransactionOrderDto.toDto(transactionOrderManager.getTransaction(id, TransactionType.PRINCIPAL_AMOUNT)));
		dto.setCommission(TransactionOrderDto.toDto(transactionOrderManager.getTransaction(id, TransactionType.CLIENT_COMMISSION_AMOUNT)));
		return dto;
	}

	@Override
	public void addPayments(List<PaymentDto> payments) {
		paymentManager.addPayments(PaymentDto.toEntities(payments));
	}
}

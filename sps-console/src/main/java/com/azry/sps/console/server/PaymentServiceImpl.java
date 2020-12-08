package com.azry.sps.console.server;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.model.payment.Payment;
import com.azry.sps.common.model.payment.PaymentStatus;
import com.azry.sps.common.model.payment.PaymentStatusLog;
import com.azry.sps.common.model.transaction.TransactionType;
import com.azry.sps.console.shared.dto.channel.ChannelDTO;
import com.azry.sps.console.shared.dto.payment.PaymentDTO;
import com.azry.sps.console.shared.dto.payment.PaymentInfoDTO;
import com.azry.sps.console.shared.dto.payment.PaymentStatusDTO;
import com.azry.sps.console.shared.dto.payment.PaymentStatusLogDTO;
import com.azry.sps.console.shared.dto.services.ServiceDTO;
import com.azry.sps.console.shared.dto.transactionorder.TransactionOrderDTO;
import com.azry.sps.console.shared.payment.PaymentParamDTO;
import com.azry.sps.console.shared.payment.PaymentService;
import com.azry.sps.server.services.channel.ChannelManager;
import com.azry.sps.server.services.payment.PaymentManager;
import com.azry.sps.server.services.service.ServiceManager;
import com.azry.sps.server.services.transactionorder.TransactionOrderManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import java.util.Date;
import java.util.List;

@WebServlet("sps/servlet/payment")
public class PaymentServiceImpl extends RemoteServiceServlet implements PaymentService {

	@Inject
	PaymentManager paymentManager;

	@Inject
	TransactionOrderManager transactionOrderManager;

	@Inject
	ServiceManager serviceManager;

	@Inject
	ChannelManager channelManager;

	@Override
	public PagingLoadResult<PaymentDTO> getPayments(int offset, int limit, PaymentParamDTO params, List<PaymentStatusDTO> statuses) {
		ListResult<Payment> res = paymentManager.getPayments(offset, limit, params.toServer(), PaymentDTO.convertDTOToStatuses(statuses));

		return new PagingLoadResultBean<>(
			PaymentDTO.toDTOs(res.getResultList()),
			res.getResultCount(),
			offset
		);
	}

	@Override
	public List<PaymentStatusLogDTO> getChanges(long paymentId) {
		return PaymentStatusLogDTO.toDTOs(paymentManager.getChanges(paymentId));
	}

	@Override
	public PaymentInfoDTO getPaymentInfo(PaymentDTO paymentDTO) {
		final PaymentInfoDTO dto = new PaymentInfoDTO();
		dto.setChannel(ChannelDTO.toDTO(channelManager.getChannel(paymentDTO.getChannelId())));
		dto.setService(ServiceDTO.toDTO(serviceManager.getService(paymentDTO.getServiceId())));
		dto.setChanges(getChanges(paymentDTO.getId()));
		dto.setPrincipal(TransactionOrderDTO.toDTO(transactionOrderManager.getTransaction(paymentDTO.getId(), TransactionType.PRINCIPAL_AMOUNT)));
		dto.setClientCommission(TransactionOrderDTO.toDTO(transactionOrderManager.getTransaction(paymentDTO.getId(), TransactionType.CLIENT_COMMISSION_AMOUNT)));
		return dto;
	}

	@Override
	public void addPayments(List<PaymentDTO> payments) {
		List<Payment> paymentList = PaymentDTO.toEntities(payments);
		paymentManager.addPayments(paymentList);
		transactionOrderManager.addTransactions(payments.get(0).getSourceAccountBan(), paymentList);
	}

	@Override
	public PaymentStatusDTO retryPayment(long paymentId) {
		paymentManager.changePaymentStatus(paymentId, PaymentStatus.PENDING);

		PaymentStatusLog log = new PaymentStatusLog();
		log.setPaymentId(paymentId);
		log.setStatus(PaymentStatus.PENDING);
		log.setStatusTime(new Date());
		log.setStatusMessage("ხელახალი მცდელობა");

		paymentManager.addPaymentStatusLog(log);
		return PaymentStatusDTO.PENDING;
	}
}

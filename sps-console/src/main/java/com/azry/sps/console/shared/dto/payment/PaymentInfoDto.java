package com.azry.sps.console.shared.dto.payment;

import com.azry.sps.common.model.payment.PaymentStatusLog;
import com.azry.sps.console.shared.dto.channel.ChannelDTO;
import com.azry.sps.console.shared.dto.services.ServiceDTO;
import com.azry.sps.console.shared.dto.transactionorder.TransactionOrderDTO;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

public class PaymentInfoDTO implements IsSerializable {

	private PaymentDTO paymentDTO;

	List<PaymentStatusLogDTO> changes;

	TransactionOrderDTO principal;

	TransactionOrderDTO clientCommission;

	ServiceDTO service;

	ChannelDTO channel;

	public ServiceDTO getService() {
		return service;
	}

	public void setService(ServiceDTO service) {
		this.service = service;
	}

	public ChannelDTO getChannel() {
		return channel;
	}

	public void setChannel(ChannelDTO channel) {
		this.channel = channel;
	}

	public PaymentDTO getPaymentDTO() {
		return paymentDTO;
	}

	public void setPaymentDTO(PaymentDTO paymentDTO) {
		this.paymentDTO = paymentDTO;
	}

	public List<PaymentStatusLogDTO> getChanges() {
		return changes;
	}

	public void setChanges(List<PaymentStatusLogDTO> changes) {
		this.changes = changes;
	}

	public TransactionOrderDTO getPrincipal() {
		return principal;
	}

	public void setPrincipal(TransactionOrderDTO principal) {
		this.principal = principal;
	}

	public TransactionOrderDTO getClientCommission() {
		return clientCommission;
	}

	public void setClientCommission(TransactionOrderDTO clientCommission) {
		this.clientCommission = clientCommission;
	}
}

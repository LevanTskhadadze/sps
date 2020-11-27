package com.azry.sps.console.shared.dto.payment;

import com.azry.sps.common.model.payment.PaymentStatusLog;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaymentStatusLogDTO implements IsSerializable {

	long id;

	long paymentId;

	PaymentStatusDTO status;

	String statusMessage;

	Date statusTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(long paymentId) {
		this.paymentId = paymentId;
	}

	public PaymentStatusDTO getStatus() {
		return status;
	}

	public void setStatus(PaymentStatusDTO status) {
		this.status = status;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public Date getStatusTime() {
		return statusTime;
	}

	public void setStatusTime(Date statusTime) {
		this.statusTime = statusTime;
	}

	@GwtIncompatible
	public static PaymentStatusLogDTO toDTO(PaymentStatusLog entity) {
		PaymentStatusLogDTO dto = new PaymentStatusLogDTO();
		if (entity == null) {
			return dto;
		}

		dto.setId(entity.getId());
		dto.setPaymentId(entity.getPaymentId());
		dto.setStatus(PaymentDTO.convertStatusToDto(entity.getStatus()));
		dto.setStatusMessage(entity.getStatusMessage());
		dto.setStatusTime(entity.getStatusTime());

		return dto;
	}

	@GwtIncompatible
	public static List<PaymentStatusLogDTO> toDTOs(List<PaymentStatusLog> entities) {
		List<PaymentStatusLogDTO> dtos = new ArrayList<>();
		if (entities == null) {
			return dtos;
		}

		for (PaymentStatusLog entity : entities) {
			dtos.add(toDTO(entity));
		}

		return dtos;
	}
}

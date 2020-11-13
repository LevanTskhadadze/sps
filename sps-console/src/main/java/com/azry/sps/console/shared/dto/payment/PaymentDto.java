package com.azry.sps.console.shared.dto.payment;

import com.azry.sps.common.model.client.Client;
import com.azry.sps.common.model.payment.Payment;
import com.azry.sps.common.model.payment.PaymentStatus;
import com.azry.sps.common.model.service.Service;
import com.azry.sps.console.shared.dto.services.ServiceChannelInfoDto;
import com.azry.sps.console.shared.dto.services.ServiceDto;
import com.google.gwt.core.shared.GwtIncompatible;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class PaymentDto {

	private long id;

	private String agentPaymentId;

	private long serviceId;

	private long channelId;

	private String abonentCode;

	private BigDecimal amount;

	private BigDecimal clCommission;

	private BigDecimal svcCommission;

	private PaymentStatusDto status;

	private Date statusChangeTime;

	private String statusMessage;

	private Date createTime;

	private Client client;

	@GwtIncompatible
	public static PaymentStatusDto convertStatusToDto(PaymentStatus status) {
		return PaymentStatusDto.values()[status.ordinal()];
	}

	@GwtIncompatible
	public static PaymentStatus convertDtoToStatus(PaymentStatusDto statusDto) {
		return PaymentStatus.values()[statusDto.ordinal()];
	}

	@GwtIncompatible
	public static PaymentDto toDto(Payment payment) {
		PaymentDto dto = new PaymentDto();

		dto.setId(payment.getId());
		dto.setAgentPaymentId(payment.getAgentPaymentId());
		dto.setServiceId(payment.getServiceId());
		dto.setChannelId(payment.getChannelId());
		dto.setAbonentCode(payment.getAbonentCode());
		dto.setAmount(payment.getAmount());
		dto.setClCommission(payment.getClCommission());
		dto.setSvcCommission(payment.getSvcCommission());
		dto.setStatus(convertStatusToDto(payment.getStatus()));
		dto.setStatusChangeTime(payment.getStatusChangeTime());
		dto.setStatusMessage(payment.getStatusMessage());
		dto.setCreateTime(payment.getCreateTime());

		return dto;
	}

	@GwtIncompatible
	public static List<PaymentDto> toDtos(List<Payment> payments) {
		List<PaymentDto> dtos = new ArrayList<>();

		for(Payment payment : payments) {
			dtos.add(toDto(payment));
		}

		return dtos;
	}

	@GwtIncompatible
	public static Payment toEntity(PaymentDto paymentDto) {
		Payment entity = new Payment();
		if(paymentDto == null) return entity;

		entity.setId(paymentDto.getId());
		entity.setAgentPaymentId(paymentDto.getAgentPaymentId());
		entity.setServiceId(paymentDto.getServiceId());
		entity.setChannelId(paymentDto.getChannelId());
		entity.setAbonentCode(paymentDto.getAbonentCode());
		entity.setAmount(paymentDto.getAmount());
		entity.setClCommission(paymentDto.getClCommission());
		entity.setSvcCommission(paymentDto.getSvcCommission());
		entity.setStatus(convertDtoToStatus(paymentDto.getStatus()));
		entity.setStatusChangeTime(paymentDto.getStatusChangeTime());
		entity.setStatusMessage(paymentDto.getStatusMessage());
		entity.setCreateTime(paymentDto.getCreateTime());
		return entity;
	}

	@GwtIncompatible
	public static List<Payment> toEntities(List<PaymentDto> paymentDtos) {
		List<Payment> entities = new ArrayList<>();
		if(paymentDtos == null) return entities;

		for(PaymentDto dto : paymentDtos) {
			entities.add(toEntity(dto));
		}

		return entities;
	}
}

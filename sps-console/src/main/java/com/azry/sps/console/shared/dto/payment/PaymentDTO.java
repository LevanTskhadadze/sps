package com.azry.sps.console.shared.dto.payment;

import com.azry.sps.common.model.payment.Payment;
import com.azry.sps.common.model.payment.PaymentStatus;
import com.azry.sps.console.shared.dto.bankclient.ClientDTO;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class PaymentDTO implements IsSerializable {

	public enum Columns {
		id,
		agentPaymentId,
		startTime,
		endTime,
		serviceId,
		channelId
	}

	public static final String DATE_PATTERN = "dd/MM/yyyy HH:mm";

	private long id;

	private String agentPaymentId;

	private long serviceId;

	private long channelId;

	private String abonentCode;

	private BigDecimal amount;

	private BigDecimal clCommission;

	private BigDecimal svcCommission;

	private PaymentStatusDTO status;

	private Date statusChangeTime;

	private String statusMessage;

	private Date createTime;

	private ClientDTO client;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAgentPaymentId() {
		return agentPaymentId;
	}

	public void setAgentPaymentId(String agentPaymentId) {
		this.agentPaymentId = agentPaymentId;
	}

	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	public String getAbonentCode() {
		return abonentCode;
	}

	public void setAbonentCode(String abonentCode) {
		this.abonentCode = abonentCode;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getClCommission() {
		return clCommission;
	}

	public void setClCommission(BigDecimal clCommission) {
		this.clCommission = clCommission;
	}

	public BigDecimal getSvcCommission() {
		return svcCommission;
	}

	public void setSvcCommission(BigDecimal svcCommission) {
		this.svcCommission = svcCommission;
	}

	public PaymentStatusDTO getStatus() {
		return status;
	}

	public void setStatus(PaymentStatusDTO status) {
		this.status = status;
	}

	public Date getStatusChangeTime() {
		return statusChangeTime;
	}

	public void setStatusChangeTime(Date statusChangeTime) {
		this.statusChangeTime = statusChangeTime;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public ClientDTO getClient() {
		return client;
	}

	public void setClient(ClientDTO client) {
		this.client = client;
	}

	@GwtIncompatible
	public static PaymentStatusDTO convertStatusToDto(PaymentStatus status) {
		return status == null ? null : PaymentStatusDTO.values()[status.ordinal()];
	}

	@GwtIncompatible
	public static PaymentStatus convertDtoToStatus(PaymentStatusDTO statusDto) {
		return statusDto == null ? null : PaymentStatus.values()[statusDto.ordinal()];
	}

	@GwtIncompatible
	public static PaymentDTO toDTO(Payment payment) {
		PaymentDTO dto = new PaymentDTO();
		if (payment == null) {
			return dto;
		}
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
		dto.setClient(ClientDTO.EntityToDTO(payment.getClient()));

		return dto;
	}

	@GwtIncompatible
	public static List<PaymentDTO> toDTOs(List<Payment> payments) {
		List<PaymentDTO> dtos = new ArrayList<>();

		for(Payment payment : payments) {
			dtos.add(toDTO(payment));
		}

		return dtos;
	}

	@GwtIncompatible
	public static Payment toEntity(PaymentDTO paymentDto) {
		Payment entity = new Payment();
		if (paymentDto == null) {
			return entity;
		}

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
		entity.setClient(paymentDto.getClient().entityFromDTO());

		return entity;
	}

	@GwtIncompatible
	public static List<Payment> toEntities(List<PaymentDTO> paymentDTOS) {
		List<Payment> entities = new ArrayList<>();
		if(paymentDTOS == null) return entities;

		for(PaymentDTO dto : paymentDTOS) {
			entities.add(toEntity(dto));
		}

		return entities;
	}

	@GwtIncompatible
	public static List<PaymentStatus> convertDTOToStatuses(List<PaymentStatusDTO> dtos) {
		List<PaymentStatus> entities = new ArrayList<>();
		if(dtos == null) return entities;

		for(PaymentStatusDTO dto : dtos) {
			entities.add(convertDtoToStatus(dto));
		}

		return entities;
	}
}

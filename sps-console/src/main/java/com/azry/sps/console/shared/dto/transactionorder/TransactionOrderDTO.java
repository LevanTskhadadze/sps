package com.azry.sps.console.shared.dto.transactionorder;

import com.azry.sps.common.model.transaction.TransactionOrder;
import com.azry.sps.common.model.transaction.TransactionType;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionOrderDTO implements IsSerializable {

	private long id;

	private String fild;

	private long paymentId;

	private String sourceAccountIBAN;

	private String destinationAccountIBAN;

	private String purpose;

	private BigDecimal amount;

	private TransactionTypeDTO type;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFild() {
		return fild;
	}

	public void setFild(String fild) {
		this.fild = fild;
	}

	public long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(long paymentId) {
		this.paymentId = paymentId;
	}

	public String getSourceAccountIBAN() {
		return sourceAccountIBAN;
	}

	public void setSourceAccountIBAN(String sourceAccountIBAN) {
		this.sourceAccountIBAN = sourceAccountIBAN;
	}

	public String getDestinationAccountIBAN() {
		return destinationAccountIBAN;
	}

	public void setDestinationAccountIBAN(String destinationAccountIBAN) {
		this.destinationAccountIBAN = destinationAccountIBAN;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public TransactionTypeDTO getType() {
		return type;
	}

	public void setType(TransactionTypeDTO type) {
		this.type = type;
	}

	@GwtIncompatible
	public static TransactionTypeDTO TypeToDTO(TransactionType type) {
			return TransactionTypeDTO.valueOf(type.name());
		}

	@GwtIncompatible
	public static TransactionType dtoToType(TransactionTypeDTO dto) {
		return TransactionType.valueOf(dto.name());
	}

	@GwtIncompatible
	public static TransactionOrderDTO toDTO(TransactionOrder entity) {
		TransactionOrderDTO dto = new TransactionOrderDTO();
		if (entity == null) {
			return dto;
		}

		dto.setId(entity.getId());
		dto.setFild(entity.getFild());
		dto.setPaymentId(entity.getPaymentId());
		dto.setSourceAccountIBAN(entity.getSourceAccountIBAN());
		dto.setDestinationAccountIBAN(entity.getDestinationAccountIBAN());
		dto.setPurpose(entity.getPurpose());
		dto.setAmount(entity.getAmount());
		dto.setType(TypeToDTO(entity.getType()));

		return dto;
	}
}

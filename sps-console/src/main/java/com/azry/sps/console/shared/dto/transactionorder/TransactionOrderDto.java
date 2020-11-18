package com.azry.sps.console.shared.dto.transactionorder;

import com.azry.sps.common.model.transaction.TransactionOrder;
import com.azry.sps.common.model.transaction.TransactionType;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionOrderDto implements IsSerializable {

		private long id;

		private String fild;

		private long paymentId;

		private String sourceAccountBAN;

		private String destinationAccountBAN;

		private String purpose;

		private BigDecimal amount;

		private TransactionTypeDto type;

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

	public String getSourceAccountBAN() {
		return sourceAccountBAN;
	}

	public void setSourceAccountBAN(String sourceAccountBAN) {
		this.sourceAccountBAN = sourceAccountBAN;
	}

	public String getDestinationAccountBAN() {
		return destinationAccountBAN;
	}

	public void setDestinationAccountBAN(String destinationAccountBAN) {
		this.destinationAccountBAN = destinationAccountBAN;
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

	public TransactionTypeDto getType() {
		return type;
	}

	public void setType(TransactionTypeDto type) {
		this.type = type;
	}

	@GwtIncompatible
		public static TransactionTypeDto TypeToDto(TransactionType type) {
			return TransactionTypeDto.valueOf(type.name());
		}

		@GwtIncompatible
		public static TransactionType dtoToType(TransactionTypeDto dto) {
			return TransactionType.valueOf(dto.name());
		}

		@GwtIncompatible
		public static TransactionOrderDto toDto(TransactionOrder entity) {
			TransactionOrderDto dto = new TransactionOrderDto();
			if (entity == null) {
				return dto;
			}

			dto.setId(entity.getId());
			dto.setFild(entity.getFild());
			dto.setPaymentId(entity.getPaymentId());
			dto.setSourceAccountBAN(entity.getSourceAccountBAN());
			dto.setDestinationAccountBAN(entity.getDestinationAccountBAN());
			dto.setPurpose(entity.getPurpose());
			dto.setAmount(entity.getAmount());
			dto.setType(TypeToDto(entity.getType()));

			return dto;
		}

}

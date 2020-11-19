package com.azry.sps.console.shared.dto.payment;

import com.azry.sps.console.shared.dto.transactionorder.TransactionOrderDto;
import com.google.gwt.user.client.rpc.IsSerializable;
import lombok.Data;

import java.util.List;

@Data
public class PaymentInfoDto implements IsSerializable {

	private PaymentDto paymentDto;

	private List<PaymentDto> changes;

	private TransactionOrderDto principal;

	private TransactionOrderDto commission;

	public PaymentDto getPaymentDto() {
		return paymentDto;
	}

	public void setPaymentDto(PaymentDto paymentDto) {
		this.paymentDto = paymentDto;
	}

	public List<PaymentDto> getChanges() {
		return changes;
	}

	public void setChanges(List<PaymentDto> changes) {
		this.changes = changes;
	}

	public TransactionOrderDto getPrincipal() {
		return principal;
	}

	public void setPrincipal(TransactionOrderDto principal) {
		this.principal = principal;
	}

	public TransactionOrderDto getCommission() {
		return commission;
	}

	public void setCommission(TransactionOrderDto commission) {
		this.commission = commission;
	}
}

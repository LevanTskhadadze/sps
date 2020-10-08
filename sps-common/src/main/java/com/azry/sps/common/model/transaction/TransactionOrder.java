package com.azry.sps.common.model.transaction;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
public class TransactionOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(length = 100)
	private String fild;

	private long paymentId;

	@Column(length = 100)
	private String sourceAccountBAN;

	@Column(length = 100)
	private String destinationAccountBAN;

	@Column(length = 50)
	private String purpose;

	@Column(precision = 10, scale = 2)
	private BigDecimal amount;

	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private TransactionType type;
}

package com.azry.sps.common.model.payment;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class PaymentStatusLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private long paymentId;

	@Column(length = 30)
	@Enumerated(EnumType.STRING)
	private PaymentStatus status;

	private Date statusTime;

	@Column(length = 500)
	private String statusMessage;
}

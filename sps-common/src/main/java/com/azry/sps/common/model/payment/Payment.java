package com.azry.sps.common.model.payment;

import com.azry.sps.common.model.client.Client;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(length = 50)
	private String agentPaymentId;

	private long serviceId;

	private long channelId;

	@Column(length = 100)
	private String abonentCode;

	@Column(precision = 10, scale = 2)
	private BigDecimal amount;

	@Column(precision = 10, scale = 2)
	private BigDecimal clCommission;

	@Column(precision = 10, scale = 2)
	private BigDecimal svcCommission;

	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private PaymentStatus status;

	private Date statusChangeTime;

	@Column(length = 500)
	private String statusMessage;

	private Date createTime;

	@Embedded
	private Client client;
}
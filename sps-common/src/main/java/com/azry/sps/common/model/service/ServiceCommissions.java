package com.azry.sps.common.model.service;

import com.azry.sps.common.model.client.CommissionRateType;
import com.azry.sps.common.Configurable;
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
public class ServiceCommissions extends Configurable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private boolean allServices;

	@Column(length = 100)
	private String servicesIds;

	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private CommissionRateType rateType;

	@Column(precision = 10, scale = 2)
	private BigDecimal minCommission;

	@Column(precision = 10, scale = 2)
	private BigDecimal maxCommission;

	@Column(precision = 10, scale = 2)
	private BigDecimal commission;
}

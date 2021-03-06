package com.azry.sps.common.model.commission;

import com.azry.sps.common.model.Configurable;
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
public class ClientCommissions extends Configurable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private long priority;

	private boolean allServices;

	@Column(length = 500)
	private String servicesIds;

	private boolean allChannels;

	@Column(length = 500)
	private String channelsIds;

	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private CommissionRateType rateType;

	@Column(precision = 10, scale = 2)
	private BigDecimal minCommission;

	@Column(precision = 10, scale = 2)
	private BigDecimal maxCommission;

	@Column(precision = 10, scale = 2)
	private BigDecimal Commission;
}

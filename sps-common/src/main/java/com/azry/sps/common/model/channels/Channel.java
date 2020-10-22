package com.azry.sps.common.model.channels;

import com.azry.sps.common.model.Configurable;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Channel extends Configurable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(length = 50)
	private String name;

	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private FiServiceUnavailabilityAction fiServiceUnavailabilityAction;

	private boolean active;
}

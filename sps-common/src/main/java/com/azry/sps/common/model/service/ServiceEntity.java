package com.azry.sps.common.model.service;

import com.azry.sps.common.model.Configurable;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class ServiceEntity extends Configurable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(length = 50)
	private String name;

	private boolean active;

	@Column(length = Integer.MAX_VALUE)
	private String data;
}

package com.azry.sps.systemparameters.model;

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
public class SystemParameter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(length = 50)
	private String code;

	@Column(length = 500)
	private String description;

	@Column(length = 100)
	private String value;

	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private SystemParameterType type;

}
